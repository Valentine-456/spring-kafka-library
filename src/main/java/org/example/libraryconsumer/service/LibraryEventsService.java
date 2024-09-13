package org.example.libraryconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.libraryconsumer.entity.LibraryEvent;
import org.example.libraryconsumer.entity.LibraryEventType;
import org.example.libraryconsumer.repository.LibraryEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;
    final Map<LibraryEventType, Consumer<LibraryEvent>>  eventHandlers = new HashMap<>();
    final ObjectMapper  objectMapper;

    public LibraryEventsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        eventHandlers.put(LibraryEventType.CREATE, this::create);
        eventHandlers.put(LibraryEventType.UPDATE, this::update);
    }

    public void processLibraryEvent(ConsumerRecord<Integer, String> record) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(record.value(), LibraryEvent.class);
        var eventType = libraryEvent.getLibraryEventType();

        var handler = eventHandlers.get(eventType);
        if (handler != null) {
            handler.accept(libraryEvent);
        } else {
            log.warn("Unknown event type");
        }

    }

    private void create(LibraryEvent libraryEvent) {
        var book = libraryEvent.getBook();
        book.setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Record successfully persisted!");
    }

    private void update(LibraryEvent libraryEvent) {
    }
}
