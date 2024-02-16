package untitled.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import untitled.ReserveApplication;
import untitled.domain.E;

@Entity
@Table(name = "Car_table")
@Data
//<<< DDD / Aggregate Root
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @PostPersist
    public void onPostPersist() {
        E e = new E(this);
        e.publishAfterCommit();
    }

    public static CarRepository repository() {
        CarRepository carRepository = ReserveApplication.applicationContext.getBean(
            CarRepository.class
        );
        return carRepository;
    }
}
//>>> DDD / Aggregate Root
