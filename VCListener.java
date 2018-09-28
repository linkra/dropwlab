package dropwlab.delaval.com.core.vc.listener;

import Delaval.VMSController.DataObject.Serializes;
import Delaval.VMSController.DataObject.SystemKeys;
import Delaval.VMSController.DataObject.VMSData;
import Delaval.VMSController.DataObject.VMSKeyValueTable;
import Delaval.VMSController.Logger.Log;
import Delaval.VMSController.VCCache.CacheFunctionType;
import Delaval.VMSController.VMSDataTransport.*;
import Delaval.VMSController.VMSDataTransport.VMSCache.CacheConnection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class VCListener implements IVMSDataEventListner {
    private static final Logger logger = Logger.getLogger(VCListener.class.getName());
    private CacheFunctionType[] functionTypes;
    private static CacheConnection cacheConnection;
    private static Semaphore semaphore;
    private static final String vcListenerName = "VCListener";
    private static final String vcIpAddress = "10.34.35.11";
    private static final int registerAttempts = 3;
    private String event;

    /*public static void main(String[] args) {
        new VCListener();
    }*/

    VCListener() {
        this.functionTypes = EventList.getEventList();
        semaphore = new Semaphore(1);
        startCacheConnection();
        registerEvents();
        startVMSDataClient();
    }

    String getEvent() {
        return event;
    }

    private void startCacheConnection() {
        cacheConnection = new CacheConnection(vcIpAddress, EndPoints.CacheServerPort, vcListenerName);
        cacheConnection.StartAndCheck();
    }

    private void registerEvents() {
        boolean initiated = false;
        try {
            attemptToRegister(initiated, registerAttempts);
        } catch (Exception e) {
            logger.severe("ERROR: Could not register event " + Log.getStackTrace(e));
        }
    }

    private void attemptToRegister(boolean initiated, int attempts) throws InterruptedException {
        while ((--attempts >= 0) && !initiated) {
            Thread.sleep(2000);
            cacheConnection.GetVMSDataClient().SetEventListner(this);
            if (registerToCache() >= functionTypes.length) {
                initiated = true;
                logger.info("All event registered successfully");
            }
        }
    }

    private void startVMSDataClient() {
        VMSDataClient vmsDataClient = new VMSDataClient(vcIpAddress, EndPoints.DeviceServerPort);
        vmsDataClient.SetSerializationType(Serializes.SerializeType.MilkStation, Serializes.SerializeType.MilkStation);
        vmsDataClient.Start(vcListenerName);
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

    @Override
    public void VMSDataEvent(VMSData vmsData, MessageSemaphore messageDispatchLock) {
        try {
            semaphore.acquire();
            semaphore.drainPermits();
            String method = vmsData.GetValue(SystemKeys._METHOD);
          if (method == null) {
                logger.info("Full sync");
                return;
            }
            if (vmsData.GetValue(SystemKeys._PRIMARY_KEY) == null) {
                return;
            }
            vmsData.SelectRow(1);  // FIXME: how to know which index to fetch?
            VMSKeyValueTable vmsKeyValueTable;
            if (!method.equals("Event")) {
                vmsKeyValueTable = vmsData.getkeyValueTable(0);
            } else {
                vmsKeyValueTable = vmsData.getkeyValueTable(1);
            }
            StringBuilder stringBuilder = new StringBuilder();
            event = serializeEvent(method, vmsKeyValueTable);
            stringBuilder.append(event);
            stringBuilder.append(" -- ");
            event = stringBuilder.toString();
        } catch (InterruptedException ex) {
            logger.info("ERROR: Failed to get lock: " + Log.getStackTrace(ex));
        } catch (Exception ex) {
            logger.info("ERROR: " + Log.getStackTrace(ex) + "\n\n" + vmsData);
        } finally {
            semaphore.release();
        }
    }

    private String serializeEvent(String method, VMSKeyValueTable vmsKeyValueTable) throws IOException, URISyntaxException {
        String serializedEvent = "";
        if (vmsKeyValueTable != null) {
            StringBuilder sb = new StringBuilder();
            vmsKeyValueTable.Serialize(sb, Serializes.SerializeType.Xml);
            serializedEvent = "<" + method + ">" + sb.toString() + "\n</" + method + ">";
            logger.info(serializedEvent);
        } else {
            logger.info("vmsKeyValueTable is " + vmsKeyValueTable);
        }
        Appender.append(serializedEvent);
        return serializedEvent;
    }

    @Override
    public void RemoteEndPointNotConnected(BasicVMSDataClient basicVMSDataClient) {
    }

    @Override
    public void RemoteEndPointConnected(BasicVMSDataClient basicVMSDataClient) {
    }

}
