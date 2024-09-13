package org.example.libraryconsumer.repository;

import org.example.libraryconsumer.entity.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryEventsRepository extends JpaRepository<LibraryEvent, Long> {
}
