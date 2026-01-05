package com.example.booklibrary.Repo;

import com.example.booklibrary.Model.Borrow;
import com.example.booklibrary.Model.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BorrowRepo extends JpaRepository<Borrow, Long> {
    Optional<Borrow> findByBorrowCodeAndDeletedAtIsNull(String borrowCode);

    List<Borrow> findByStatusAndDeletedAtIsNull(BorrowStatus status);

    Page<Borrow> findByStatusAndDeletedAtIsNull(BorrowStatus status, Pageable pageable);

    List<Borrow> findByMemberIdAndDeletedAtIsNull(Long memberId);

    long countByMemberIdAndStatusAndDeletedAtIsNull(Long memberId, BorrowStatus status);

    List<Borrow> findByBookIdAndDeletedAtIsNull(Long bookId);

    Optional<Borrow> findTopByOrderByIdDesc();

    boolean existsByBookIdAndStatusAndDeletedAtIsNull(Long bookId, BorrowStatus status);

    Optional<Borrow> findByBookIdAndStatusAndDeletedAtIsNull(Long bookId, BorrowStatus status);

    List<Borrow> findByReturnDateIsNullAndDeletedAtIsNull();

    List<Borrow> findByReturnDateIsNotNullAndDeletedAtIsNull();

    List<Borrow> findAllByDeletedAtIsNull();

    Page<Borrow> findAllByDeletedAtIsNull(Pageable pageable);
}
