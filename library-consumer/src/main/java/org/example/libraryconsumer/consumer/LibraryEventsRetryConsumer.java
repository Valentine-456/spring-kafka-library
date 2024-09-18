package org.example.libraryconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.libraryconsumer.service.LibraryEventsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class LibraryEventsRetryConsumer {
    private LibraryEventsService libraryEventsService;

    @KafkaListener(
            topics = {"${topics.retry}"},
            groupId = "retry-listener-group"
    )
    public void onMessage(ConsumerRecord<Integer, String> record) throws JsonProcessingException {
        log.info("Retrying message: {}", record);
        libraryEventsService.processLibraryEvent(record);
    }
}
