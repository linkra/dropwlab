package dropwlab.delaval.com.core.vc;

import Delaval.VMSController.DataObject.Serializes;
import Delaval.VMSController.DataObject.SystemKeys;
import Delaval.VMSController.DataObject.VMSData;
import Delaval.VMSController.Logger.Log;
import Delaval.VMSController.VCCache.CacheFunctionType;
import Delaval.VMSController.VMSDataTransport.*;
import Delaval.VMSController.VMSDataTransport.VMSCache.CacheConnection;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class VCListener implements IVMSDataEventListner {

    private CacheFunctionType[] functionTypes;
    private static CacheConnection cache;
    private static VMSDataClient dsp;
    private static Semaphore criticalSection;
    private static final SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String VCListenerName = "VCListener";
    private static final int registerAttempts = 3;

    public VCListener() {
        String vcIpAddress = "localhost";
        cache = new CacheConnection(vcIpAddress, EndPoints.CacheServerPort, VCListenerName);
        cache.StartAndCheck();
        registerEvents();
        dsp = new VMSDataClient(vcIpAddress, EndPoints.DeviceServerPort);
        dsp.SetSerializationType(Serializes.SerializeType.MilkStation, Serializes.SerializeType.MilkStation);
        dsp.Start(VCListenerName);
        EventList eventList = new EventList();
        this.functionTypes = eventList.getEventList();
        criticalSection = new Semaphore(1);
    }

    private void stop() {
        cache.Stop();
        dsp.Stop();
        cache = null;
        dsp = null;
    }

    private void registerEvents() {
        boolean notInitiated = true;
        try {
            attemptToRegister(notInitiated, registerAttempts);
        } catch (Exception e) {
            err("Could not register events " + Log.getStackTrace(e));
        } finally {
            stop();
        }
    }

    private void attemptToRegister(boolean notInitiated, int attempts) throws InterruptedException {
        while ((--attempts >= 0) && notInitiated) {
            Thread.sleep(2000);
            cache.GetVMSDataClient().SetEventListner(this);
            if (registerToCache() >= functionTypes.length) {
                notInitiated = false;
                out("All events registrated ok");
            }
        }
    }

    private int registerToCache() {
        int i = -1;
        while (++i < functionTypes.length) {
            if (!cache.RegisterEventInCache(functionTypes[i])) {
                err("Cannot register " + functionTypes[i] + " Event");
                break;
            }
        }
        return i;
    }

    /**
     * Event handler.
     *
     * @param vmsData   The event data.
     * @param messageDispatchLock of type MessageSemaphore
     */
    @Override
    public void VMSDataEvent(VMSData vmsData, MessageSemaphore messageDispatchLock) {
        try {
            criticalSection.acquire();
            criticalSection.drainPermits();
            if (getMethod(vmsData) == null) {
                return;
            }
            if (vmsData.GetValue(SystemKeys._PRIMARY_KEY) == null) {
                return;
            }
            vmsData.SelectRow(1); // TODO: why?
            logKeyValueTable(vmsData, getMethod(vmsData));
        } catch (InterruptedException ex) {
            err("Failed to get lock: " + Log.getStackTrace(ex));
        } catch (Exception e) {
            err(Log.getStackTrace(e) + "\n\n" + vmsData);
        } finally {
            criticalSection.release();
        }
    }

    private String getMethod(VMSData vmsData) {
        String method = vmsData.GetValue(SystemKeys._METHOD);
        if (method == null || !method.contains("Event")) {
            out("Full sync");
            return null;
        }
        return method;
    }

    private void logKeyValueTable(VMSData vmsData, String method) {
        StringBuilder sb = new StringBuilder();
        vmsData.getkeyValueTable(1).Serialize(sb, Serializes.SerializeType.Xml);
        String serializedData = sb.toString();
        serializedData = "<" + method + ">\n" + serializedData + "\n</" + method + ">";
        out(serializedData);
    }

    private static void out(String log) {
        System.out.println(dateFormat.format(LocalDateTime.now() + " " + log));
    }

    private static void err(String log) {
        System.err.println(dateFormat.format(LocalDateTime.now() + " " + log));
    }

    @Override
    public void RemoteEndPointNotConnected(BasicVMSDataClient basicVMSDataClient) {
    }

    @Override
    public void RemoteEndPointConnected(BasicVMSDataClient basicVMSDataClient) {
    }

}
