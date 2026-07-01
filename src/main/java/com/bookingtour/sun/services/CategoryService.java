package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.AdminCreateCategoryRequest;
import com.bookingtour.sun.dto.request.CategorySearchRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.CategoryResponse;
import com.bookingtour.sun.entity.Category;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.repository.CategoryRepository;
import com.bookingtour.sun.repository.TourRepository;
import jakarta.transaction.Transactional;
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
    private final TourRepository tourRepository;

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

    public void create(AdminCreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
    }

    @Transactional
    public void update(Long id, AdminCreateCategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        if (tourRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Không thể xóa danh mục này vì đang có tour sử dụng");
        }
        categoryRepository.delete(category);
    }


    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
