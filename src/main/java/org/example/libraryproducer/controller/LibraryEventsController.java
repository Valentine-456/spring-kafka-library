package org.example.libraryproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.libraryproducer.domain.LibraryEvent;
import org.example.libraryproducer.producer.LibraryEventsProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@AllArgsConstructor
public class LibraryEventsController {

    private final LibraryEventsProducer producer;

    @PostMapping("/v1/library-event")
    public ResponseEntity<LibraryEvent> postLibraryEvent(
            @RequestBody @Valid LibraryEvent libraryEvent
    ) throws JsonProcessingException, ExecutionException, InterruptedException {

//        producer.sendLibraryEvent(libraryEvent);
//        producer.sendLibraryEventSync(libraryEvent);
        producer.sendLibraryEventWithHeaders(libraryEvent);

        log.info("library event: {}", libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
