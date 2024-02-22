package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OrderPause extends AbstractEvent {

    private Long id;
    private Integer qty;
    private String airobotId;
    private String airobotName;
    private String userId;
    private String address;
}
