package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderCancel extends AbstractEvent {

    private Long id;

    public OrderCancel(Order aggregate) {
        super(aggregate);
    }

    public OrderCancel() {
        super();
    }
}
//>>> DDD / Domain Event
