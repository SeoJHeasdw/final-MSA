package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class E extends AbstractEvent {

    private Long id;

    public E(Car aggregate) {
        super(aggregate);
    }

    public E() {
        super();
    }
}
//>>> DDD / Domain Event
