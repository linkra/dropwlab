package dropwlab.delaval.com.core.service;

import Delaval.VMSController.DataObject.VMSData;
import Delaval.VMSController.VMSDataTransport.MessageSemaphore;
import dropwlab.delaval.com.api.Event;
import dropwlab.delaval.com.core.vc.VCListener;

import java.util.List;

public class EventService {
    private final VCListener vcListener;
    private final VMSData vmsData;
    private final MessageSemaphore messageDispatchLock;

    public EventService(VCListener vcListener, VMSData vmsData, MessageSemaphore messageDispatchLock) {
        this.vcListener = vcListener;
        this.vmsData = vmsData;
        this.messageDispatchLock = messageDispatchLock;
    }

    public List<Event> findAll() {
        vcListener.VMSDataEvent(this.vmsData,  this.messageDispatchLock);
        return null;
    }
}
