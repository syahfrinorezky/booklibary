package com.example.booklibrary.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booklibrary.Model.Books;
import java.util.List;
import java.util.Optional;

public interface BooksRepo extends JpaRepository<Books, Long> {
    Optional<Books> findByBookCodeAndDeletedAtIsNull(String bookCode);

    Optional<Books> findByIsbnAndDeletedAtIsNull(String isbn);

    List<Books> findByTitleContainingIgnoreCaseAndDeletedAtIsNull(String title);

    List<Books> findByAuthorContainingIgnoreCaseAndDeletedAtIsNull(String author);

    Optional<Books> findTopByOrderByIdDesc();

    List<Books> findAllByDeletedAtIsNull();
}
