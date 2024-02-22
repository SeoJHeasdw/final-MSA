package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderPause extends AbstractEvent {

    private Long id;
    private Integer qty;
    private String airobotId;
    private String airobotName;
    private String userId;
    private String address;

    public OrderPause(Order aggregate) {
        super(aggregate);
    }

    public OrderPause() {
        super();
    }
}
//>>> DDD / Domain Event
