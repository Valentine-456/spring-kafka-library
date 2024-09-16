package org.example.libraryconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.libraryconsumer.entity.Book;
import org.example.libraryconsumer.entity.LibraryEvent;
import org.example.libraryconsumer.entity.LibraryEventType;
import org.example.libraryconsumer.repository.BookRepository;
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
    @Autowired
    private BookRepository bookRepository;
    final Map<LibraryEventType, Consumer<LibraryEvent>> eventHandlers = new HashMap<>();
    final ObjectMapper objectMapper;

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
        Book book = libraryEvent.getBook();

        if (book.getBookId() == null) {
            book = bookRepository.save(book);
            libraryEvent.setBook(book);

            libraryEventsRepository.save(libraryEvent);
            log.info("Record successfully persisted!");
        }
    }

    @Transactional
    protected void update(LibraryEvent libraryEvent) {
        String newLibraryEventTypeString = libraryEvent.getLibraryEventType().toString();

        libraryEventsRepository.update_library_event_and_book(
                libraryEvent.getBook().getBookId(),
                libraryEvent.getBook().getBookName(),
                libraryEvent.getBook().getBookTitle(),
                newLibraryEventTypeString
        );
        log.info("Library event and book updated successfully!");
    }
}
