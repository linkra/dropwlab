package dropwlab.delaval.com.resources;

import com.codahale.metrics.annotation.Timed;
import dropwlab.delaval.com.api.Event;
import dropwlab.delaval.com.core.service.EventService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/vc-event")
@Produces(MediaType.APPLICATION_JSON)
public class VCResource {
    private final AtomicLong counter;
    private final EventService eventService;

    public VCResource(AtomicLong counter, EventService eventService) {
        this.counter = counter;
        this.eventService = eventService;
    }

    @GET
    @Timed
    public List<Event>  listEvents() {
        return eventService.findAll();
    }
}
