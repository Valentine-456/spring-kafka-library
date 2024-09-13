package org.example.libraryconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "library_event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private LibraryEventType libraryEventType;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "libraryEvent")
    @ToString.Exclude
    private Book book;
}