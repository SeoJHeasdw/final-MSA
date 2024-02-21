package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentStopped extends AbstractEvent {

    private Long id;

    public PaymentStopped(Payment aggregate) {
        super(aggregate);
    }

    public PaymentStopped() {
        super();
    }
}
//>>> DDD / Domain Event
