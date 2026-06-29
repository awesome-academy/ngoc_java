package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.CategorySearchRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.CategoryResponse;
import com.bookingtour.sun.entity.Category;
import com.bookingtour.sun.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .toList();
    }

    public PageResponse<CategoryResponse> searchCategory(CategorySearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<Category> page;
        if (request.getName() == null || request.getName().isBlank()) {
            page = categoryRepository.findAll(pageable);
        } else {
            page = categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    request.getName(),
                    request.getName(),
                    pageable
            );
        }

        List<CategoryResponse> categories = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponse.<CategoryResponse>builder()
                .content(categories)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
