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

    public InstallCompleted(Install aggregate) {
        super(aggregate);
    }

    public InstallCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
