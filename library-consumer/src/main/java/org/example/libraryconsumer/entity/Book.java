package org.example.libraryconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer bookId;

    private String bookName;

    private String bookTitle;

}
