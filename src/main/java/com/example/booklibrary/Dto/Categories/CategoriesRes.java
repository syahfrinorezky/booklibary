package com.example.booklibrary.Dto.Categories;

import java.time.LocalDateTime;

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
public class CategoriesRes {
    private Long id;
	private String categoryCode;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
