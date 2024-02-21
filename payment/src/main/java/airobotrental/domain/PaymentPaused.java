package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentPaused extends AbstractEvent {

    private Long id;

    public PaymentPaused(Payment aggregate) {
        super(aggregate);
    }

    public PaymentPaused() {
        super();
    }
}
//>>> DDD / Domain Event
