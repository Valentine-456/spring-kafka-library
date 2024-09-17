package org.example.libraryconsumer.repository;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.libraryconsumer.entity.LibraryEvent;
import org.example.libraryconsumer.entity.LibraryEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface LibraryEventsRepository extends JpaRepository<LibraryEvent, Integer> {
    @Modifying
    @Procedure(name = "update_library_event_and_book")
    void update_library_event_and_book(
            @Param("p_book_id") Integer bookId,
            @Param("p_book_name") String bookName,
            @Param("p_book_title") String bookTitle,
            @Param("p_new_library_event_type") String newLibraryEventType
    );
}
