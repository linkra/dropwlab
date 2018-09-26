package dropwlab.delaval.com.core.vc.listener;

public class EventService { // TODO: create interface

    public EventService() {
        // TODO: make class members instead
    }

   // public List<Event> listenToEvents() { // TODO: make list of string
    public String listenToEvents() throws InterruptedException {
        VCListener vcListener = new VCListener();
        Thread.sleep(50000);
        // FIXME: listen to stream of events
        String events = vcListener.getEvent();
        return events != null && !events.isEmpty() ?  events : "no events";
    }
}
