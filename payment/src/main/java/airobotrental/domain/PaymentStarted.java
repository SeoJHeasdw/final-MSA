package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentStarted extends AbstractEvent {

    private Long id;

    public PaymentStarted(Payment aggregate) {
        super(aggregate);
    }

    public PaymentStarted() {
        super();
    }
}
//>>> DDD / Domain Event
