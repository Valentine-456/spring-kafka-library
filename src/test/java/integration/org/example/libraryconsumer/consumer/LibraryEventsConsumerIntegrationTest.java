package org.example.libraryconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.libraryconsumer.entity.Book;
import org.example.libraryconsumer.entity.LibraryEvent;
import org.example.libraryconsumer.entity.LibraryEventType;
import org.example.libraryconsumer.service.LibraryEventsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}", "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})

public class LibraryEventsConsumerIntegrationTest {
    @Autowired
    EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;
    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    @Autowired
    ObjectMapper objectMapper;
    @SpyBean
    LibraryEventsConsumer libraryEventsConsumerSpy;
    @SpyBean
    LibraryEventsService libraryEventsServiceSpy;

    private final LibraryEvent libraryEvent = new LibraryEvent(
            123,
            LibraryEventType.UPDATE,
            new Book(300, "Name" , "Title")
    );

    @BeforeEach
    public void setUp() {
        for(MessageListenerContainer container : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
        }
    }

    @Test
    void publishNewLibraryEvent() throws JsonProcessingException, ExecutionException, InterruptedException {
        String eventString = objectMapper.writeValueAsString(libraryEvent);
        kafkaTemplate.sendDefault(eventString).get();

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        verify(libraryEventsConsumerSpy, times(1)).onMessage(isA(ConsumerRecord.class));
        verify(libraryEventsServiceSpy, times(1)).processLibraryEvent(isA(ConsumerRecord.class));
    }
}
