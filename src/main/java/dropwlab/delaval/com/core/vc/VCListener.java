package dropwlab.delaval.com.core.vc;

import Delaval.VMSController.DataObject.Serializes;
import Delaval.VMSController.DataObject.SystemKeys;
import Delaval.VMSController.DataObject.VMSData;
import Delaval.VMSController.DataObject.VMSKeyValueTable;
import Delaval.VMSController.Logger.Log;
import Delaval.VMSController.VCCache.CacheFunctionType;
import Delaval.VMSController.VMSDataTransport.*;
import Delaval.VMSController.VMSDataTransport.VMSCache.CacheConnection;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class VCListener implements IVMSDataEventListner {
    private static final Logger logger = Logger.getLogger(VCListener.class.getName());
    private CacheFunctionType[] functionTypes;
    private static CacheConnection cacheConnection;
    private static VMSDataClient vmsDataClient;
    private static Semaphore criticalSectionSemaphore;
    private static final String VCListenerName = "VCListenerLina";
    private static final String vcIpAddress = "10.34.35.11";
    private static final int registerAttempts = 3;
    private String events;

    public static void main(String[] args) {
        new VCListener();
    }

    VCListener() {
        this.functionTypes = EventList.getEventList();
        criticalSectionSemaphore = new Semaphore(1);

        startCacheConnection();
        registerEvents();
        startVMSDataClient();
    }

    public String getEvents() {
        return events;
    }

    private void startCacheConnection() {
        cacheConnection = new CacheConnection(vcIpAddress, EndPoints.CacheServerPort, VCListenerName);
        cacheConnection.StartAndCheck();
    }

    private void startVMSDataClient() {
        vmsDataClient = new VMSDataClient(vcIpAddress, EndPoints.DeviceServerPort);
        vmsDataClient.SetSerializationType(Serializes.SerializeType.MilkStation, Serializes.SerializeType.MilkStation);
        vmsDataClient.Start(VCListenerName);
    }

    private void registerEvents() {
        boolean initiated = false;
        try {
            attemptToRegister(initiated, registerAttempts);
        } catch (Exception e) {
            logger.severe("ERROR: Could not register events " + Log.getStackTrace(e));
        } finally {
            stop();
        }
    }

    private void attemptToRegister(boolean initiated, int attempts) throws InterruptedException {
        while ((--attempts >= 0) && !initiated) {
            Thread.sleep(2000);
            cacheConnection.GetVMSDataClient().SetEventListner(this);
            if (registerToCache() >= functionTypes.length) {
                initiated = true;
                logger.info("All events registered successfully");
            }
        }
    }

    private int registerToCache() {
        int numberOfRegistered;
        int i = -1;
        while (++i < functionTypes.length) {
            logger.info("register " + functionTypes[i] + "\n");
            if (!cacheConnection.RegisterEventInCache(functionTypes[i])) {
                logger.info("ERROR: Cannot register " + functionTypes[i] + " event");
                break;
            }
        }
        numberOfRegistered = i;
        return numberOfRegistered;
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
            criticalSectionSemaphore.acquire();
            criticalSectionSemaphore.drainPermits();
            if (getMethod(vmsData) == null) {
                return;
            }
            if (vmsData.GetValue(SystemKeys._PRIMARY_KEY) == null) {
                return;
            }
            vmsData.SelectRow(1);
            events = logKeyValueTable(vmsData, getMethod(vmsData));
        } catch (InterruptedException ex) {
            logger.info("ERROR: Failed to get lock: " + Log.getStackTrace(ex));
        } catch (Exception e) {
            logger.info("ERROR: " + Log.getStackTrace(e) + "\n\n" + vmsData);
        } finally {
            criticalSectionSemaphore.release();
        }
    }

    private String getMethod(VMSData vmsData) {
        String method = vmsData.GetValue(SystemKeys._METHOD);
        if (method == null || !method.contains("Event")) {
            logger.info("Full sync");
            return null;
        }
        return method;
    }

    private String logKeyValueTable(VMSData vmsData, String method) {
        StringBuilder sb = new StringBuilder();
        VMSKeyValueTable vmsKeyValueTable = vmsData.getkeyValueTable(1);
        if (vmsKeyValueTable != null) {
            vmsKeyValueTable.Serialize(sb, Serializes.SerializeType.Xml);
            String serializedTableContent = sb.toString();
            serializedTableContent = "<" + method + ">\n" + serializedTableContent + "\n</" + method + ">";
            logger.info(serializedTableContent);
            return serializedTableContent;
        }
        logger.info(" ---------------->> vmsData.getkeyValueTable(1) returned " + vmsKeyValueTable);
        return "";
    }

    private void stop() {
        if (cacheConnection != null) {
            cacheConnection.Stop();
            cacheConnection = null;
        }
        if (vmsDataClient != null) {
            vmsDataClient.Stop();
            vmsDataClient = null;
        }
    }

    @Override
    public void RemoteEndPointNotConnected(BasicVMSDataClient basicVMSDataClient) {
    }

    @Override
    public void RemoteEndPointConnected(BasicVMSDataClient basicVMSDataClient) {
    }

}
