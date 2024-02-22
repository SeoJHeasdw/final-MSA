package airobotrental.infra;

import airobotrental.config.kafka.KafkaProcessor;
import airobotrental.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderPause'"
    )
    public void wheneverOrderPause_PaymentPause(
        @Payload OrderPause orderPause
    ) {
        OrderPause event = orderPause;
        System.out.println(
            "\n\n##### listener PaymentPause : " + orderPause + "\n\n"
        );

        // Sample Logic //
        Payment.paymentPause(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='InstallCompleted'"
    )
    public void wheneverInstallCompleted_PaymentStart(
        @Payload InstallCompleted installCompleted
    ) {
        InstallCompleted event = installCompleted;
        System.out.println(
            "\n\n##### listener PaymentStart : " + installCompleted + "\n\n"
        );

        // Sample Logic //
        Payment.paymentStart(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='UninstallCompleted'"
    )
    public void wheneverUninstallCompleted_PaymentStop(
        @Payload UninstallCompleted uninstallCompleted
    ) {
        UninstallCompleted event = uninstallCompleted;
        System.out.println(
            "\n\n##### listener PaymentStop : " + uninstallCompleted + "\n\n"
        );

        // Sample Logic //
        Payment.paymentStop(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
