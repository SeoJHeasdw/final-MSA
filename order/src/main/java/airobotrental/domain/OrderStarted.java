package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderStarted extends AbstractEvent {

    private Long id;

    public OrderStarted(Order aggregate) {
        super(aggregate);
    }

    public OrderStarted() {
        super();
    }
}
//>>> DDD / Domain Event
