package com.example.booklibrary.Model;

import com.example.booklibrary.Model.Base.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Books extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_code", nullable = false, unique = true, length = 20)
    private String bookCode;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(length = 100)
    private String publisher;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer stock;
}
