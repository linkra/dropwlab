package dropwlab.delaval.com.resources;

import com.codahale.metrics.annotation.Timed;
import dropwlab.delaval.com.core.vc.EventService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Path("/vc-event")
@Produces(MediaType.APPLICATION_JSON)
public class VCResource {
    private static final Logger logger = Logger.getLogger(VCResource.class.getName());
    private final AtomicLong counter;
    private final EventService eventService;

    public VCResource(AtomicLong counter, EventService eventService) {
        this.counter = counter;
        this.eventService = eventService;
    }

    @GET
    @Timed
    public void listEvents() throws InterruptedException {
        eventService.listenToEvents();
    }
}
