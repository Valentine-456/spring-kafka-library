package org.example.libraryproducer.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Book(
        Integer bookId,
        @NotBlank
        String bookName,
        @NotBlank
        String bookTitle
) {
}
