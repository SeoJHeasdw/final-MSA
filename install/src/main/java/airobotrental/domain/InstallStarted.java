package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class InstallStarted extends AbstractEvent {

    private Long id;
    private String airobotId;
    private Integer qty;
    private String airobotName;
    
    public InstallStarted(Install aggregate) {
        super(aggregate);
    }

    public InstallStarted() {
        super();
    }
}
//>>> DDD / Domain Event
