package org.example.libraryconsumer.repository;

import org.example.libraryconsumer.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}