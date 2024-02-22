package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OrderCancel extends AbstractEvent {

    private Long id;
    private String airobotId;
    private Integer qty;
    private String airobotName;
}
