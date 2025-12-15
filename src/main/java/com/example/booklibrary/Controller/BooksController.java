package com.example.booklibrary.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booklibrary.Dto.ApiResponse.ApiResponse;
import com.example.booklibrary.Dto.Books.BooksReq;
import com.example.booklibrary.Dto.Books.BooksRes;
import com.example.booklibrary.Service.BooksService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BooksRes>>> getAllBooks() {
        List<BooksRes> books = booksService.getAllBooks();
        return ResponseEntity.ok(new ApiResponse<>(true, "Books retrieved successfully", books));
    }

    @GetMapping("/{bookCode}")
    public ResponseEntity<ApiResponse<BooksRes>> getBookByCode(@PathVariable String bookCode) {
        BooksRes book = booksService.getBookByCode(bookCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book retrieved successfully", book));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BooksRes>> createBook(@RequestBody @Valid BooksReq req) {
        BooksRes book = booksService.createBook(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book created successfully", book));
    }

    @PatchMapping("/{bookCode}")
    public ResponseEntity<ApiResponse<BooksRes>> updateBook(@PathVariable String bookCode,
            @RequestBody @Valid BooksReq req) {
        BooksRes book = booksService.updateBook(bookCode, req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book updated successfully", book));
    }

    @DeleteMapping("/{bookCode}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable String bookCode) {
        booksService.deleteBook(bookCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book deleted successfully", null));
    }
}
