package com.example.booklibrary.Dto.ApiResponse;

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
public class PaginationMeta {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}
