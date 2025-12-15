package com.example.booklibrary.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booklibrary.Model.Categories;

public interface CategoriesRepo extends JpaRepository<Categories, Long> {
    Optional<Categories> findTopByOrderByIdDesc();

    Optional<Categories> findByCategoryCodeAndDeletedAtIsNull(String categoryCode);

    Optional<Categories> findByNameAndDeletedAtIsNull(String name);

    List<Categories> findAllByDeletedAtIsNull();
}
