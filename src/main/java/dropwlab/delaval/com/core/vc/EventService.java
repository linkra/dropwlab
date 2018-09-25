package dropwlab.delaval.com.core.vc;

public class EventService { // TODO: create interface

    public EventService() {
        // TODO: make class members instead
    }

   // public List<Event> listenToEvents() { // TODO: make list of string
    public void listenToEvents() throws InterruptedException {
        VCListener vcListener = new VCListener();
        Thread.sleep(20000);
        String events = vcListener.getEvents();
        //return events != null && !events.isEmpty() ?  events : "no events";
    }
}
