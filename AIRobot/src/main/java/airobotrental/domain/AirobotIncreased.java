package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AirobotIncreased extends AbstractEvent {

    private Long id;

    public AirobotIncreased(Airobot aggregate) {
        super(aggregate);
    }

    public AirobotIncreased() {
        super();
    }
}
//>>> DDD / Domain Event
