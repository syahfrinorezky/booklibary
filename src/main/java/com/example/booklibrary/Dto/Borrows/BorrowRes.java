package com.example.booklibrary.Dto.Borrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.booklibrary.Model.BorrowStatus;

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
public class BorrowRes {
    private Long id;
    private String borrowCode;
    private String memberCode;
    private String memberName;
    private String bookCode;
    private String bookTitle;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private BorrowStatus status;
    private String notes;
    private BigDecimal penaltyFee;
}
