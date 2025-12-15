package com.example.booklibrary.Repo;

import com.example.booklibrary.Model.Borrow;
import com.example.booklibrary.Model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BorrowRepo extends JpaRepository<Borrow, Long> {
    Optional<Borrow> findByBorrowCodeAndDeletedAtIsNull(String borrowCode);

    List<Borrow> findByStatusAndDeletedAtIsNull(BorrowStatus status);

    List<Borrow> findByMemberIdAndDeletedAtIsNull(Long memberId);

    List<Borrow> findByBookIdAndDeletedAtIsNull(Long bookId);

    List<Borrow> findAllByDeletedAtIsNull();
}
