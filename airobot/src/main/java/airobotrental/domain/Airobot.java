package airobotrental.domain;

import airobotrental.AirobotApplication;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Airobot_table")
@Data
//<<< DDD / Aggregate Root
public class Airobot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long airobotId;

    private String airobotName;

    private Integer stock;

    private String useStatus;

    public static AirobotRepository repository() {
        AirobotRepository airobotRepository = AirobotApplication.applicationContext.getBean(
            AirobotRepository.class
        );
        return airobotRepository;
    }

    //<<< Clean Arch / Port Method
    public static void airobotDecrease(InstallStarted installStarted) {
        //implement business logic here:

        /** Example 1:  new item 
        Airobot airobot = new Airobot();
        repository().save(airobot);

        */

        /** Example 2:  finding and process
        
        repository().findById(installStarted.get???()).ifPresent(airobot->{
            
            airobot // do something
            repository().save(airobot);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void airobotIncrease(UninstallStarted uninstallStarted) {
        //implement business logic here:

        /** Example 1:  new item 
        Airobot airobot = new Airobot();
        repository().save(airobot);

        */

        /** Example 2:  finding and process
        
        repository().findById(uninstallStarted.get???()).ifPresent(airobot->{
            
            airobot // do something
            repository().save(airobot);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void useState(PaymentStarted paymentStarted) {
        //implement business logic here:

        /** Example 1:  new item 
        Airobot airobot = new Airobot();
        repository().save(airobot);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentStarted.get???()).ifPresent(airobot->{
            
            airobot // do something
            repository().save(airobot);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void useState(PaymentStopped paymentStopped) {
        //implement business logic here:

        /** Example 1:  new item 
        Airobot airobot = new Airobot();
        repository().save(airobot);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentStopped.get???()).ifPresent(airobot->{
            
            airobot // do something
            repository().save(airobot);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void useState(PaymentPaused paymentPaused) {
        //implement business logic here:

        /** Example 1:  new item 
        Airobot airobot = new Airobot();
        repository().save(airobot);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentPaused.get???()).ifPresent(airobot->{
            
            airobot // do something
            repository().save(airobot);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
