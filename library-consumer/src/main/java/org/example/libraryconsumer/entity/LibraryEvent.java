package org.example.libraryconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "library_event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LibraryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer libraryEventId;

    @Enumerated(EnumType.STRING)
    private LibraryEventType libraryEventType;

    @ManyToOne // Change to ManyToOne
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private Book book;
}