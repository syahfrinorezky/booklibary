package com.example.booklibrary.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.booklibrary.Dto.ApiResponse.PageResponse;
import com.example.booklibrary.Dto.ApiResponse.PaginationMeta;
import com.example.booklibrary.Dto.Books.BooksReq;
import com.example.booklibrary.Dto.Books.BooksRes;
import com.example.booklibrary.Model.Books;
import com.example.booklibrary.Model.Categories;
import com.example.booklibrary.Repo.BooksRepo;
import com.example.booklibrary.Repo.CategoriesRepo;

import jakarta.transaction.Transactional;

@Service
public class BooksService {
    private final BooksRepo booksRepo;
    private final CategoriesRepo categoriesRepo;

    public BooksService(BooksRepo booksRepo, CategoriesRepo categoriesRepo) {
        this.booksRepo = booksRepo;
        this.categoriesRepo = categoriesRepo;
    }

    private String generateBookCode() {
        Books lastBook = booksRepo.findTopByOrderByIdDesc().orElse(null);
        int nextNumber = (lastBook == null) ? 1 : lastBook.getId().intValue() + 1;
        return String.format("BK%04d", nextNumber);
    }

    private BooksRes mapToRes(Books book) {
        return BooksRes.builder()
                .id(book.getId())
                .bookCode(book.getBookCode())
                .categoryName(book.getCategory().getName())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishedYear(book.getPublishedYear())
                .description(book.getDescription())
                .stock(book.getStock())
                .createdAt(book.getCreatedAt())
                .build();
    }

    public List<BooksRes> getAllBooks() {
        return booksRepo.findAllByDeletedAtIsNull().stream()
                .map(this::mapToRes)
                .toList();
    }

    public PageResponse<BooksRes> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Books> booksPage = booksRepo.findAllByDeletedAtIsNull(pageable);

        List<BooksRes> booksResList = booksPage.getContent().stream()
                .map(this::mapToRes)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .page(booksPage.getNumber())
                .size(booksPage.getSize())
                .totalPages(booksPage.getTotalPages())
                .totalElements(booksPage.getTotalElements())
                .build();

        return PageResponse.<BooksRes>builder()
                .content(booksResList)
                .meta(meta)
                .build();
    }

    public BooksRes getBookByCode(String bookCode) {
        Books book = booksRepo.findByBookCodeAndDeletedAtIsNull(bookCode)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToRes(book);
    }

    @Transactional
    public BooksRes createBook(BooksReq req) {
        Categories category = categoriesRepo.findByCategoryCodeAndDeletedAtIsNull(req.getCategoryCode())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (booksRepo.findByIsbnAndDeletedAtIsNull(req.getIsbn()).isPresent()) {
            throw new RuntimeException("Book with this ISBN already exists");
        }

        Books book = new Books();
        book.setBookCode(generateBookCode());
        book.setCategory(category);
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setPublisher(req.getPublisher());
        book.setPublishedYear(req.getPublishedYear());
        book.setDescription(req.getDescription());
        book.setStock(req.getStock());

        booksRepo.save(book);
        return mapToRes(book);
    }

    @Transactional
    public BooksRes updateBook(String bookCode, BooksReq req) {
        Books book = booksRepo.findByBookCodeAndDeletedAtIsNull(bookCode)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Categories category = categoriesRepo.findByCategoryCodeAndDeletedAtIsNull(req.getCategoryCode())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!book.getIsbn().equals(req.getIsbn())
                && booksRepo.findByIsbnAndDeletedAtIsNull(req.getIsbn()).isPresent()) {
            throw new RuntimeException("Book with this ISBN already exists");
        }

        book.setCategory(category);
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setPublisher(req.getPublisher());
        book.setPublishedYear(req.getPublishedYear());
        book.setDescription(req.getDescription());
        book.setStock(req.getStock());

        booksRepo.save(book);
        return mapToRes(book);
    }

    @Transactional
    public void deleteBook(String bookCode) {
        Books book = booksRepo.findByBookCodeAndDeletedAtIsNull(bookCode)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setDeletedAt(LocalDateTime.now());
        booksRepo.save(book);
    }
}
