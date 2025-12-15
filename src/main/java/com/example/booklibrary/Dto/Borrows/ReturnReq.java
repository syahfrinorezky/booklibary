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
public class ReturnReq {
    @NotBlank(message = "Borrow code is required")
    private String borrowCode;

    private String returnNotes;
}
