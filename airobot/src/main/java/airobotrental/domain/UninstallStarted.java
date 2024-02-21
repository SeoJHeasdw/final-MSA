package airobotrental.domain;

import airobotrental.domain.*;
import airobotrental.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class UninstallStarted extends AbstractEvent {

    private Long id;
    private Long airobotId;
    private Integer qty;
    private Integer stock;

}
