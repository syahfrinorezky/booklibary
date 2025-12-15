package com.example.booklibrary.Dto.Books;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BooksRes {
    private Long id;
    private String bookCode;
    private String categoryName;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Integer publishedYear;
    private String description;
    private Integer stock;
    private LocalDateTime createdAt;
}
