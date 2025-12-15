package com.example.booklibrary.Model;

import com.example.booklibrary.Model.Base.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "borrows")
public class Borrow extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "borrow_code", nullable = false, unique = true, length = 20)
    private String borrowCode;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @Column(name = "borrow_date", nullable = false)
    private LocalDateTime borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BorrowStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "penalty_fee", precision = 10, scale = 2)
    private BigDecimal penaltyFee;
}
