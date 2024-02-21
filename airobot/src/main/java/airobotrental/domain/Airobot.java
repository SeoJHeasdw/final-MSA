package airobotrental.domain;

import airobotrental.AirobotApplication;
import airobotrental.domain.AirobotDecreased;
import airobotrental.domain.AirobotIncreased;
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
    private Long id;

    private String airobotName;

    private Integer stock;

    private String useStatus;

    private Long airobotId;

    @PostPersist
    public void onPostPersist() {
        AirobotDecreased airobotDecreased = new AirobotDecreased(this);
        airobotDecreased.publishAfterCommit();

        AirobotIncreased airobotIncreased = new AirobotIncreased(this);
        airobotIncreased.publishAfterCommit();
    }

    public static AirobotRepository repository() {
        AirobotRepository airobotRepository = AirobotApplication.applicationContext.getBean(
            AirobotRepository.class
        );
        return airobotRepository;
    }

    //<<< Clean Arch / Port Method
    public static void airobotDecrease(InstallStarted installStarted) {
        /** fill out following code  */
        
        repository().findById(Long.valueOf(installStarted.getAirobotId())).ifPresent(airobot->{
            airobot.setStock(airobot.getStock() - installStarted.getQty());
            repository().save(airobot);

        });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void airobotIncrease(UninstallStarted uninstallStarted) {
        repository().findById(Long.valueOf(uninstallStarted.getAirobotId())).ifPresent(airobot->{
            airobot.setStock(airobot.getStock() + uninstallStarted.getQty());
            repository().save(airobot);

        });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void useState(PaymentStarted paymentStarted) {
        repository().findById(Long.valueOf(paymentStarted.getId())).ifPresent(airobot->{
            airobot.setUseStatus("Y");
            repository().save(airobot);

        });
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void useState(PaymentStopped paymentStopped) {
        repository().findById(Long.valueOf(paymentStopped.getId())).ifPresent(airobot->{
            airobot.setUseStatus("N");
            repository().save(airobot);

        });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void useState(PaymentPaused paymentPaused) {
        repository().findById(Long.valueOf(paymentPaused.getId())).ifPresent(airobot->{
            airobot.setUseStatus("N");
            repository().save(airobot);

        });
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
