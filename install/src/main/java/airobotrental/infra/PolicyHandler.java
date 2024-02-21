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
    InstallRepository installRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderStarted'"
    )
    public void wheneverOrderStarted_InstallBegins(
        @Payload OrderStarted orderStarted
    ) {
        OrderStarted event = orderStarted;
        System.out.println(
            "\n\n##### listener InstallBegins : " + orderStarted + "\n\n"
        );

        // Sample Logic //
        Install.installBegins(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderCancel'"
    )
    public void wheneverOrderCancel_UninstallBegins(
        @Payload OrderCancel orderCancel
    ) {
        OrderCancel event = orderCancel;
        System.out.println(
            "\n\n##### listener UninstallBegins : " + orderCancel + "\n\n"
        );

        // Sample Logic //
        Install.uninstallBegins(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
