package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class InstallCompleted extends AbstractEvent {

    private Long id;
    private Integer qty;
    private Boolean deliveryStatus;
    private String address;
    private String orderId;
    private String airobotId;
    private String deliveryId;

    public InstallCompleted(Install aggregate) {
        super(aggregate);
    }

    public InstallCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
