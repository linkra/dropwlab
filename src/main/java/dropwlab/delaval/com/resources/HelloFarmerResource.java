package dropwlab.delaval.com.resources;

import com.codahale.metrics.annotation.Timed;
import dropwlab.delaval.com.api.Saying;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-farmer")
@Produces(MediaType.APPLICATION_JSON)
public class HelloFarmerResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloFarmerResource(String template, String defaultName, AtomicLong counter) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = counter;
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }
}
