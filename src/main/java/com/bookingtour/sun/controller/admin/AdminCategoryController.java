package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.CategorySearchRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.CategoryResponse;
import com.bookingtour.sun.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public String listCategory(@ModelAttribute CategorySearchRequest request, Model model) {
        model.addAttribute("title", "Manage Category");
        model.addAttribute("activeMenu", "categories");
        PageResponse<CategoryResponse> pageResponse = categoryService.searchCategory(request);

        model.addAttribute("categories", pageResponse.getContent());
        // Pagination and filter info for template
        model.addAttribute("pageNumber", pageResponse.getPage() + 1);
        model.addAttribute("pageSize", pageResponse.getSize());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("currentName", request.getName() != null ? request.getName() : "");

        return "admin/categories/index";
    }
}
