package com.example.booklibrary.Dto.Borrows;

import jakarta.validation.constraints.NotBlank;
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
public class BorrowReq {
    @NotBlank(message = "Member code is required")
    private String memberCode;

    @NotBlank(message = "Book code is required")
    private String bookCode;

    private String notes;
}
