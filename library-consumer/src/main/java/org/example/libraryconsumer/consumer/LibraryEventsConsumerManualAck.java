package org.example.libraryconsumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class LibraryEventsConsumerManualAck implements AcknowledgingMessageListener<Integer, String> {

    @Override
//    @KafkaListener(topics = {"library-events"})
    public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
        log.info("Received Message: {}", data);
        acknowledgment.acknowledge();
    }
}
