package dropwlab.delaval.com.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class Event {
    private long id;

    @Length(max = 255)
    private String event;

    public Event() {
    }

    @JsonProperty
    public String getEvent() {
        return event;
    }

    @JsonProperty
    public long getId() {
        return id;
    }
}
