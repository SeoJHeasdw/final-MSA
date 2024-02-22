package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class UninstallCompleted extends AbstractEvent {

    private Long id;
    private Integer qty;
    private Boolean deliveryStatus;
    private String address;
    private String orderId;
    private String airobotId;
    private String deliveryId;
}
