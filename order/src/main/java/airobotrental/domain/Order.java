package airobotrental.domain;

import airobotrental.OrderApplication;
import airobotrental.domain.OrderCancel;
import airobotrental.domain.OrderModifed;
import airobotrental.domain.OrderPaused;
import airobotrental.domain.OrderStarted;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Order_table")
@Data
//<<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    private String airobotName;

    private Integer qty;

    @PostPersist
    public void onPostPersist() {
        OrderStarted orderStarted = new OrderStarted(this);
        orderStarted.publishAfterCommit();

        OrderModifed orderModifed = new OrderModifed(this);
        orderModifed.publishAfterCommit();

        OrderPaused orderPaused = new OrderPaused(this);
        orderPaused.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        OrderCancel orderCancel = new OrderCancel(this);
        orderCancel.publishAfterCommit();
    
    }

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(
            OrderRepository.class
        );
        return orderRepository;
    }

    //<<< Clean Arch / Port Method
    public static void notiyForUser(PaymentPaused paymentPaused) {
        // notiy

    }

    public static void notiyForUser(PaymentStarted paymentStarted) {
        // notiy

    }

    public static void notiyForUser(PaymentStopped paymentStopped) {
        // notiy
    }

    
    public static void notiyForUser(InstallStarted installStarted) {
        // notiy

    }

    public static void notiyForUser(UninstallStarted uninstallStarted) {
        // notiy

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
