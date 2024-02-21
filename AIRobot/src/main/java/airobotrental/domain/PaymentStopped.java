package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class PaymentStopped extends AbstractEvent {

    private Long id;
}
