package com.example.booklibrary.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.booklibrary.Dto.ApiResponse.PageResponse;
import com.example.booklibrary.Dto.ApiResponse.PaginationMeta;
import com.example.booklibrary.Dto.Categories.CategoriesReq;
import com.example.booklibrary.Dto.Categories.CategoriesRes;
import com.example.booklibrary.Model.Categories;
import com.example.booklibrary.Repo.CategoriesRepo;

@Service
public class CategoriesService {
    private final CategoriesRepo categoriesRepo;

    private String generateCode() {
        Categories lastCategory = categoriesRepo.findTopByOrderByIdDesc().orElse(null);
        int nextNumber = (lastCategory == null) ? 1 : lastCategory.getId().intValue() + 1;
        return String.format("CAT%02d", nextNumber);
    }

    private CategoriesRes mapToRes(Categories category) {
        return CategoriesRes.builder()
                .id(category.getId())
                .categoryCode(category.getCategoryCode())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public CategoriesService(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }

    public List<CategoriesRes> getAllCategories() {
        List<Categories> categories = categoriesRepo.findAllByDeletedAtIsNull();
        return categories.stream().map(this::mapToRes).toList();
    }

    public PageResponse<CategoriesRes> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Categories> categoriesPage = categoriesRepo.findAllByDeletedAtIsNull(pageable);

        List<CategoriesRes> categoriesResList = categoriesPage.getContent().stream()
                .map(this::mapToRes)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .page(categoriesPage.getNumber())
                .size(categoriesPage.getSize())
                .totalPages(categoriesPage.getTotalPages())
                .totalElements(categoriesPage.getTotalElements())
                .build();

        return PageResponse.<CategoriesRes>builder()
                .content(categoriesResList)
                .meta(meta)
                .build();
    }

    public CategoriesRes getCategoryByCode(String categoryCode) {
        Categories category = categoriesRepo.findByCategoryCodeAndDeletedAtIsNull(categoryCode)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToRes(category);
    }

    public CategoriesRes createCategory(CategoriesReq dto) {
        if (dto.getCategoryCode() != null
                && categoriesRepo.findByCategoryCodeAndDeletedAtIsNull(dto.getCategoryCode()).isPresent()) {
            throw new RuntimeException("Category code already exists");
        }

        Categories category = new Categories();
        if (dto.getCategoryCode() == null || dto.getCategoryCode().isEmpty()) {
            category.setCategoryCode(generateCode());
        } else {
            category.setCategoryCode(dto.getCategoryCode());
        }
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        Categories savedCategory = categoriesRepo.save(category);
        return mapToRes(savedCategory);
    }

    public CategoriesRes updateCategory(String categoryCode, CategoriesReq dto) {
        Categories category = categoriesRepo.findByCategoryCodeAndDeletedAtIsNull(categoryCode)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (dto.getName() != null)
            category.setName(dto.getName());
        if (dto.getDescription() != null)
            category.setDescription(dto.getDescription());

        Categories updatedCategory = categoriesRepo.save(category);
        return mapToRes(updatedCategory);
    }

    public void deleteCategory(String categoryCode) {
        Categories category = categoriesRepo.findByCategoryCodeAndDeletedAtIsNull(categoryCode)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setDeletedAt(LocalDateTime.now());
        categoriesRepo.save(category);
    }
}
