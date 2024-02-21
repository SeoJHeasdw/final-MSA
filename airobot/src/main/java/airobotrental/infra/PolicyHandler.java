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
    AirobotRepository airobotRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='InstallStarted'"
    )
    public void wheneverInstallStarted_AirobotDecrease(
        @Payload InstallStarted installStarted
    ) {
        InstallStarted event = installStarted;
        System.out.println(
            "\n\n##### listener AirobotDecrease : " + installStarted + "\n\n"
        );

        // Sample Logic //
        Airobot.airobotDecrease(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='UninstallStarted'"
    )
    public void wheneverUninstallStarted_AirobotIncrease(
        @Payload UninstallStarted uninstallStarted
    ) {
        UninstallStarted event = uninstallStarted;
        System.out.println(
            "\n\n##### listener AirobotIncrease : " + uninstallStarted + "\n\n"
        );

        // Sample Logic //
        Airobot.airobotIncrease(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentStarted'"
    )
    public void wheneverPaymentStarted_UseState(
        @Payload PaymentStarted paymentStarted
    ) {
        PaymentStarted event = paymentStarted;
        System.out.println(
            "\n\n##### listener UseState : " + paymentStarted + "\n\n"
        );

        // Sample Logic //
        Airobot.useState(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentStopped'"
    )
    public void wheneverPaymentStopped_UseState(
        @Payload PaymentStopped paymentStopped
    ) {
        PaymentStopped event = paymentStopped;
        System.out.println(
            "\n\n##### listener UseState : " + paymentStopped + "\n\n"
        );

        // Sample Logic //
        Airobot.useState(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentPaused'"
    )
    public void wheneverPaymentPaused_UseState(
        @Payload PaymentPaused paymentPaused
    ) {
        PaymentPaused event = paymentPaused;
        System.out.println(
            "\n\n##### listener UseState : " + paymentPaused + "\n\n"
        );

        // Sample Logic //
        Airobot.useState(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
