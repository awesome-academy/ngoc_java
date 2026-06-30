package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.AdminCreateCategoryRequest;
import com.bookingtour.sun.dto.request.CategorySearchRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.CategoryResponse;
import com.bookingtour.sun.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        loadCategoryPage(request, model);
        model.addAttribute("category", new AdminCreateCategoryRequest());
        model.addAttribute("editCategory", new AdminCreateCategoryRequest());
        return "admin/categories/index";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("category") AdminCreateCategoryRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        CategorySearchRequest searchRequest = new CategorySearchRequest();
        if (result.hasErrors()) {
            model.addAttribute("category", request);
            model.addAttribute("editCategory", new AdminCreateCategoryRequest());
            loadCategoryPage(searchRequest, model);
            model.addAttribute("openCreateModal", true);
            return "admin/categories/index";
        }

        categoryService.create(request);

        redirectAttributes.addFlashAttribute("successMessage", "Category created successfully.");

        return "redirect:/admin/categories";
    }

    @PutMapping("/{id}")
    public String updateCategory(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("editCategory")
            AdminCreateCategoryRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        CategorySearchRequest searchRequest = new CategorySearchRequest();
        if(result.hasErrors()) {
            loadCategoryPage(searchRequest, model);
            model.addAttribute("editId", id);
            model.addAttribute("editCategory", request);
            model.addAttribute("category", new AdminCreateCategoryRequest());

            model.addAttribute("openEditModal", true);
            return "admin/categories/index";
        }

        try {
            categoryService.update(id, request);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Update category successfully"
            );

        } catch(Exception e){
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );

        }

        return "redirect:/admin/categories";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            categoryService.deleteCategory(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Category deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete failed: " + e.getMessage());
        }

        return "redirect:/admin/categories";
    }

    private void loadCategoryPage(CategorySearchRequest request, Model model) {

        model.addAttribute("title", "Manage Category");
        model.addAttribute("activeMenu", "categories");

        PageResponse<CategoryResponse> pageResponse = categoryService.searchCategory(request);

        model.addAttribute("categories", pageResponse.getContent());

        model.addAttribute("pageNumber", pageResponse.getPage() + 1);
        model.addAttribute("pageSize", pageResponse.getSize());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());

        model.addAttribute(
                "currentName",
                request.getName() == null ? "" : request.getName()
        );
    }

}
