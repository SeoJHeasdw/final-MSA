package airobotrental.domain;

import airobotrental.PaymentApplication;
import airobotrental.domain.PaymentPaused;
import airobotrental.domain.PaymentStarted;
import airobotrental.domain.PaymentStopped;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Payment_table")
@Data
//<<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long orderId;

    private Long deliveryId;

    private String useStatus;

    private String delivertStatus;

    private Long airobotId;

    @PostPersist
    public void onPostPersist() {
        PaymentPaused paymentPaused = new PaymentPaused(this);
        paymentPaused.publishAfterCommit();

        PaymentStarted paymentStarted = new PaymentStarted(this);
        paymentStarted.publishAfterCommit();

        PaymentStopped paymentStopped = new PaymentStopped(this);
        paymentStopped.publishAfterCommit();
    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
            PaymentRepository.class
        );
        return paymentRepository;
    }

    //<<< Clean Arch / Port Method
    public static void paymentPause(OrderPaused orderPaused) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        */

        /** Example 2:  finding and process
        
        repository().findById(orderPaused.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void paymentStart(InstallCompleted installCompleted) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        */

        /** Example 2:  finding and process
        
        repository().findById(installCompleted.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void paymentStop(UninstallCompleted uninstallCompleted) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        */

        /** Example 2:  finding and process
        
        repository().findById(uninstallCompleted.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
