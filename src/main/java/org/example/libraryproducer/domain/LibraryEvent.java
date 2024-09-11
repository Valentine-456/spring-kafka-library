package org.example.libraryproducer.domain;

public record LibraryEvent(
        Integer libraryEventId,
        LibraryEventType libraryEventType,
        Book book
) {
}
