package org.example.libraryproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.libraryproducer.domain.LibraryEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class LibraryEventsProducer {
    @Value("${spring.kafka.topic}")
    private String topicName;
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(final KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        var future = kafkaTemplate.send(topicName, key, value);

        return future.whenComplete(
                (sendResult, exception) -> {
                    if (exception != null) {
                        handleException(key, value, exception);
                    } else {
                        handleSuccess(key, value, sendResult);
                    }
                }
        );
    }

    public SendResult<Integer, String> sendLibraryEventSync(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        var sendResult = kafkaTemplate.send(topicName, key, value).get();
        handleSuccess(key, value, sendResult);
        return sendResult;
    }

    public CompletableFuture<SendResult<Integer, String>> sendLibraryEventWithHeaders(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);
        var record = buildProducerRecord(key, value);

        var future = kafkaTemplate.send(record);

        return future.whenComplete(
                (sendResult, exception) -> {
                    if (exception != null) {
                        handleException(key, value, exception);
                    } else {
                        handleSuccess(key, value, sendResult);
                    }
                }
        );
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {
        List<Header> headers = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topicName, null, key, value, headers);
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Successfully sent library event: \n key: {}\n value: {}\n partition No: {}\n",
                key,
                value,
                sendResult.getRecordMetadata().partition());
    }

    private void handleException(Integer key, String value, Throwable exception) {
        log.error("Error sending library event: {}", exception.getMessage(), exception);
    }

}
