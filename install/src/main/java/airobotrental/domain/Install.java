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

    private Boolean deliveryStatus;

    private String address;

    private String orderId;

    private String airobotId;

    private String deliveryId;

    private String userId;

    @PostPersist
    public void onPostPersist() {
        InstallStarted installStarted = new InstallStarted(this);
        installStarted.publishAfterCommit();

        UninstallStarted uninstallStarted = new UninstallStarted(this);
        uninstallStarted.publishAfterCommit();

        UninstallCompleted uninstallCompleted = new UninstallCompleted(this);
        uninstallCompleted.publishAfterCommit();

        InstallCompleted installCompleted = new InstallCompleted(this);
        installCompleted.publishAfterCommit();
    }

    public static InstallRepository repository() {
        InstallRepository installRepository = InstallApplication.applicationContext.getBean(
            InstallRepository.class
        );
        return installRepository;
    }

    //<<< Clean Arch / Port Method
    public static void installBegins(OrderStarted orderStarted) {
        //implement business logic here:

        /** Example 1:  new item 
        Install install = new Install();
        repository().save(install);

        */

        /** Example 2:  finding and process
        
        repository().findById(orderStarted.get???()).ifPresent(install->{
            
            install // do something
            repository().save(install);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void uninstallBegins(OrderCancel orderCancel) {
        //implement business logic here:

        /** Example 1:  new item 
        Install install = new Install();
        repository().save(install);

        */

        /** Example 2:  finding and process
        
        repository().findById(orderCancel.get???()).ifPresent(install->{
            
            install // do something
            repository().save(install);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
