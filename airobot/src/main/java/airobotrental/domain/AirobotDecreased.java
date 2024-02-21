package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AirobotDecreased extends AbstractEvent {

    private Long id;

    public AirobotDecreased(Airobot aggregate) {
        super(aggregate);
    }

    public AirobotDecreased() {
        super();
    }
}
//>>> DDD / Domain Event
