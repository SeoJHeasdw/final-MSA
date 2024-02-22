package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class UninstallStarted extends AbstractEvent {

    private Long id;
    private String airobotId;
    private Integer qty;
    private String airobotName;

    public UninstallStarted(Install aggregate) {
        super(aggregate);
    }

    public UninstallStarted() {
        super();
    }
}
//>>> DDD / Domain Event
