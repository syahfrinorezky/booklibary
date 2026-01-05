package com.example.booklibrary.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.booklibrary.Dto.ApiResponse.ApiResponse;
import com.example.booklibrary.Dto.ApiResponse.PageResponse;
import com.example.booklibrary.Dto.Borrows.BorrowReq;
import com.example.booklibrary.Dto.Borrows.BorrowRes;
import com.example.booklibrary.Dto.Borrows.ReturnReq;
import com.example.booklibrary.Service.BorrowService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {
    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BorrowRes>>> getAllBorrows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<BorrowRes> borrows = borrowService.getAllBorrows(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "All borrow records retrieved successfully", borrows));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PageResponse<BorrowRes>>> getActiveBorrows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<BorrowRes> borrows = borrowService.getActiveBorrows(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Active borrows retrieved successfully", borrows));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<BorrowRes>>> getBorrowHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<BorrowRes> borrows = borrowService.getBorrowHistory(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Returned borrows history retrieved successfully", borrows));
    }

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<BorrowRes>> borrowBook(@RequestBody @Valid BorrowReq req) {
        BorrowRes borrow = borrowService.borrowBook(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book borrowed successfully", borrow));
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse<BorrowRes>> returnBook(@RequestBody @Valid ReturnReq req) {
        BorrowRes borrow = borrowService.returnBook(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book returned successfully", borrow));
    }
}
