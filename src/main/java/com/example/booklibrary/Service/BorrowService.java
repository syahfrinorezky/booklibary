package com.example.booklibrary.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.booklibrary.Dto.ApiResponse.PageResponse;
import com.example.booklibrary.Dto.ApiResponse.PaginationMeta;
import com.example.booklibrary.Dto.Borrows.BorrowReq;
import com.example.booklibrary.Dto.Borrows.BorrowRes;
import com.example.booklibrary.Dto.Borrows.ReturnReq;
import com.example.booklibrary.Model.Books;
import com.example.booklibrary.Model.Borrow;
import com.example.booklibrary.Model.BorrowStatus;
import com.example.booklibrary.Model.Members;
import com.example.booklibrary.Repo.BooksRepo;
import com.example.booklibrary.Repo.BorrowRepo;
import com.example.booklibrary.Repo.MembersRepo;

import jakarta.transaction.Transactional;

@Service
public class BorrowService {
    private final BorrowRepo borrowRepo;
    private final BooksRepo booksRepo;
    private final MembersRepo membersRepo;

    private static final int LOAN_DAYS = 7;
    private static final int MAX_BORROW_LIMIT = 2;
    private static final BigDecimal DAILY_PENALTY = new BigDecimal("5000");

    public BorrowService(BorrowRepo borrowRepo, BooksRepo booksRepo, MembersRepo membersRepo) {
        this.borrowRepo = borrowRepo;
        this.booksRepo = booksRepo;
        this.membersRepo = membersRepo;
    }

    private String generateBorrowCode() {
        Borrow lastBorrow = borrowRepo.findTopByOrderByIdDesc().orElse(null);
        int nextNumber = (lastBorrow == null) ? 1 : lastBorrow.getId().intValue() + 1;
        return String.format("BR%04d", nextNumber);
    }

    private BorrowRes mapToRes(Borrow borrow) {
        return BorrowRes.builder()
                .id(borrow.getId())
                .borrowCode(borrow.getBorrowCode())
                .memberCode(borrow.getMember().getMemberCode())
                .memberName(borrow.getMember().getName())
                .bookCode(borrow.getBook().getBookCode())
                .bookTitle(borrow.getBook().getTitle())
                .borrowDate(borrow.getBorrowDate())
                .dueDate(borrow.getDueDate())
                .returnDate(borrow.getReturnDate())
                .status(borrow.getStatus())
                .notes(borrow.getNotes())
                .penaltyFee(borrow.getPenaltyFee())
                .build();
    }

    public List<BorrowRes> getAllBorrows() {
        return borrowRepo.findAllByDeletedAtIsNull().stream()
                .map(this::mapToRes)
                .toList();
    }

    public PageResponse<BorrowRes> getAllBorrows(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("borrowDate").descending());
        Page<Borrow> borrowPage = borrowRepo.findAllByDeletedAtIsNull(pageable);
        return mapToPageResponse(borrowPage);
    }

    public List<BorrowRes> getActiveBorrows() {
        return borrowRepo.findByStatusAndDeletedAtIsNull(BorrowStatus.BORROWED).stream()
                .map(this::mapToRes)
                .toList();
    }

    public PageResponse<BorrowRes> getActiveBorrows(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("borrowDate").descending());
        Page<Borrow> borrowPage = borrowRepo.findByStatusAndDeletedAtIsNull(BorrowStatus.BORROWED, pageable);
        return mapToPageResponse(borrowPage);
    }

    public List<BorrowRes> getBorrowHistory() {
        return borrowRepo.findByStatusAndDeletedAtIsNull(BorrowStatus.RETURNED).stream()
                .map(this::mapToRes)
                .toList();
    }

    public PageResponse<BorrowRes> getBorrowHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("returnDate").descending());
        Page<Borrow> borrowPage = borrowRepo.findByStatusAndDeletedAtIsNull(BorrowStatus.RETURNED, pageable);
        return mapToPageResponse(borrowPage);
    }

    private PageResponse<BorrowRes> mapToPageResponse(Page<Borrow> borrowPage) {
        List<BorrowRes> borrowResList = borrowPage.getContent().stream()
                .map(this::mapToRes)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .page(borrowPage.getNumber())
                .size(borrowPage.getSize())
                .totalPages(borrowPage.getTotalPages())
                .totalElements(borrowPage.getTotalElements())
                .build();

        return PageResponse.<BorrowRes>builder()
                .content(borrowResList)
                .meta(meta)
                .build();
    }

    @Transactional
    public BorrowRes borrowBook(BorrowReq req) {
        Members member = membersRepo.findByMemberCodeAndDeletedAtIsNull(req.getMemberCode())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Books book = booksRepo.findByBookCodeAndDeletedAtIsNull(req.getBookCode())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStock() <= 0) {
            throw new RuntimeException("Book is out of stock");
        }

        long activeBorrows = borrowRepo.countByMemberIdAndStatusAndDeletedAtIsNull(member.getId(),
                BorrowStatus.BORROWED);
        if (activeBorrows >= MAX_BORROW_LIMIT) {
            throw new RuntimeException("Member has reached the maximum borrow limit of " + MAX_BORROW_LIMIT + " books");
        }

        boolean alreadyBorrowed = borrowRepo.findByMemberIdAndDeletedAtIsNull(member.getId()).stream()
                .anyMatch(b -> b.getBook().getId().equals(book.getId()) && b.getStatus() == BorrowStatus.BORROWED);

        if (alreadyBorrowed) {
            throw new RuntimeException("Member already has this book borrowed");
        }

        Borrow borrow = new Borrow();
        borrow.setBorrowCode(generateBorrowCode());
        borrow.setMember(member);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setDueDate(LocalDateTime.now().plusDays(LOAN_DAYS));
        borrow.setStatus(BorrowStatus.BORROWED);
        borrow.setNotes(req.getNotes());

        book.setStock(book.getStock() - 1);
        booksRepo.save(book);

        borrowRepo.save(borrow);
        return mapToRes(borrow);
    }

    @Transactional
    public BorrowRes returnBook(ReturnReq req) {
        Borrow borrow = borrowRepo.findByBorrowCodeAndDeletedAtIsNull(req.getBorrowCode())
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        if (borrow.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        borrow.setReturnDate(LocalDateTime.now());
        borrow.setStatus(BorrowStatus.RETURNED);

        if (req.getReturnNotes() != null && !req.getReturnNotes().isEmpty()) {
            String existingNotes = borrow.getNotes() != null ? borrow.getNotes() : "";
            borrow.setNotes(existingNotes + " | Return Notes: " + req.getReturnNotes());
        }

        if (borrow.getReturnDate().isAfter(borrow.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(borrow.getDueDate(), borrow.getReturnDate());
            if (daysLate > 0) {
                borrow.setPenaltyFee(DAILY_PENALTY.multiply(BigDecimal.valueOf(daysLate)));
            } else {
                borrow.setPenaltyFee(BigDecimal.ZERO);
            }
        } else {
            borrow.setPenaltyFee(BigDecimal.ZERO);
        }

        Books book = borrow.getBook();
        book.setStock(book.getStock() + 1);
        booksRepo.save(book);

        borrowRepo.save(borrow);
        return mapToRes(borrow);
    }
}
