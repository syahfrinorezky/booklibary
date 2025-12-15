package com.example.booklibrary.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booklibrary.Dto.ApiResponse.ApiResponse;
import com.example.booklibrary.Dto.Categories.CategoriesReq;
import com.example.booklibrary.Dto.Categories.CategoriesRes;
import com.example.booklibrary.Service.CategoriesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriesRes>>> getAllCategories() {
        List<CategoriesRes> categories = categoriesService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories retrieved successfully", categories));
    }

    @GetMapping("/{categoryCode}")
    public ResponseEntity<ApiResponse<CategoriesRes>> getCategoryByCode(@PathVariable String categoryCode) {
        CategoriesRes category = categoriesService.getCategoryByCode(categoryCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category retrieved successfully", category));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoriesRes>> createCategory(@RequestBody @Valid CategoriesReq dto) {
        CategoriesRes category = categoriesService.createCategory(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category created successfully", category));
    }

    @PatchMapping("/{categoryCode}")
    public ResponseEntity<ApiResponse<CategoriesRes>> updateCategory(@PathVariable String categoryCode,
            @RequestBody @Valid CategoriesReq dto) {
        CategoriesRes category = categoriesService.updateCategory(categoryCode, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category updated successfully", category));
    }

    @DeleteMapping("/{categoryCode}")
    public ResponseEntity<ApiResponse<CategoriesRes>> deleteCategory(@PathVariable String categoryCode) {
        categoriesService.deleteCategory(categoryCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully", null));
    }
}
