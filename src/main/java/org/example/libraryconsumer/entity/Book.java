package org.example.libraryconsumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String bookName;

    private String bookTitle;

    @OneToOne
    @JoinColumn(name = "library_event_id")
    private LibraryEvent libraryEvent;
}
