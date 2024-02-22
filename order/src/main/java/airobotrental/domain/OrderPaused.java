package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderPaused extends AbstractEvent {

    private Long id;
    private Long airobotId;
    private Integer qty;
    private Integer stock;

    public OrderPaused(Order aggregate) {
        super(aggregate);
    }

    public OrderPaused() {
        super();
    }
}
//>>> DDD / Domain Event
