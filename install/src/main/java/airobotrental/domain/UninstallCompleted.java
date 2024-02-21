package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class UninstallCompleted extends AbstractEvent {

    private Long id;

    public UninstallCompleted(Install aggregate) {
        super(aggregate);
    }

    public UninstallCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
