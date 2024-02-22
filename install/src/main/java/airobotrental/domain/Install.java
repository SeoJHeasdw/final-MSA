package airobotrental.domain;

import airobotrental.InstallApplication;
import airobotrental.domain.InstallCompleted;
import airobotrental.domain.InstallStarted;
import airobotrental.domain.UninstallCompleted;
import airobotrental.domain.UninstallStarted;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Install_table")
@Data
//<<< DDD / Aggregate Root
public class Install {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer qty;

    private String deliveryStatus;

    private String address;

    private Long orderId;

    private Long airobotId;

    private Long deliveryId;

    @PostPersist
    public void onPostPersist() {
        // InstallStarted installStarted = new InstallStarted(this);
        // installStarted.publishAfterCommit();
    
        // UninstallStarted uninstallStarted = new UninstallStarted(this);
        // uninstallStarted.publishAfterCommit();
    
        // InstallCompleted installCompleted = new InstallCompleted(this);
        // installCompleted.publishAfterCommit();
    
        // UninstallCompleted uninstallCompleted = new UninstallCompleted(this);
        // uninstallCompleted.publishAfterCommit();
    }

    public static InstallRepository repository() {
        InstallRepository installRepository = InstallApplication.applicationContext.getBean(
            InstallRepository.class
        );
        return installRepository;
    }

    public static void installBegins(OrderStarted orderStarted) {
        Long id = orderStarted.getId();
        int qty = orderStarted.getQty();
        String airobotId = orderStarted.getAirobotId();
        String airobotName = orderStarted.getAirobotName();

        // ai로봇 서비스로 정보 전송(id,qty,airobotId,airobotName)
        InstallStarted installStarted = new InstallStarted();
        installStarted.setId(id);
        installStarted.setQty(qty);
        installStarted.setAirobotId(airobotId);
        installStarted.setAirobotName(airobotName);

        installStarted.publishAfterCommit();

    }

    public static void uninstallBegins(OrderCancel orderCancel) {
        Long id = orderCancel.getId();
        int qty = orderCancel.getQty();
        String airobotId = orderCancel.getAirobotId();
        String airobotName = orderCancel.getAirobotName();

         // ai로봇 서비스로 정보 전송(id,qty,airobotId,airobotName)
        UninstallStarted uninstallStarted = new UninstallStarted();
        uninstallStarted.setId(id);
        uninstallStarted.setQty(qty);
        uninstallStarted.setAirobotId(airobotId);
        uninstallStarted.setAirobotName(airobotName);

        uninstallStarted.publishAfterCommit();

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
