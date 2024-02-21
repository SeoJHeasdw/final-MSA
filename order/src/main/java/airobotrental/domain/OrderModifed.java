package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderModifed extends AbstractEvent {

    private Long id;

    public OrderModifed(Order aggregate) {
        super(aggregate);
    }

    public OrderModifed() {
        super();
    }
}
//>>> DDD / Domain Event
