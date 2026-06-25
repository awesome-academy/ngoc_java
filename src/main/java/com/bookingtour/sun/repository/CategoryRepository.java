package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description,
            Pageable pageable
    );
}
