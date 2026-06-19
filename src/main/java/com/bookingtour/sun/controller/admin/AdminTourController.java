package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.CreateTourRequest;
import com.bookingtour.sun.dto.request.EditTourRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.request.TourSearchRequest;
import com.bookingtour.sun.dto.response.TourResponse;
import com.bookingtour.sun.enums.TourStatus;
import com.bookingtour.sun.services.CategoryService;
import com.bookingtour.sun.services.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tours")
@RequiredArgsConstructor
@Slf4j
public class AdminTourController {
    private final TourService tourService;
    private final CategoryService categoryService;

    @GetMapping("/new")
    public String createTour(Model model) {
        model.addAttribute("title", "Create Tour");
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tour", new CreateTourRequest());
        model.addAttribute("statuses", TourStatus.values());
        return "admin/tours/new";
    }

    @GetMapping()
    public String listTours(@ModelAttribute TourSearchRequest searchForm, Model model) {
        model.addAttribute("title", "Manage Tours");
        PageResponse<TourResponse> pageResponse = tourService.searchTours(searchForm);

        model.addAttribute("tours", pageResponse.getContent());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("statuses", TourStatus.values());

        // Pagination and filter info for template
        model.addAttribute("pageNumber", pageResponse.getPage() + 1);
        model.addAttribute("pageSize", pageResponse.getSize());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("currentSearch", searchForm.getName() != null ? searchForm.getName() : "");
        model.addAttribute("currentStatus", searchForm.getStatus() != null ? searchForm.getStatus().toString() : "");
        model.addAttribute("currentCategory", searchForm.getCategoryId() != null ? searchForm.getCategoryId() : "");

        return "admin/tours/index";
    }

    /**
     * CREATE Tour
     * POST /tours
     */
    @PostMapping
    public String addTour(
            @Valid
            @ModelAttribute("tour")
            CreateTourRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
            ) {

        log.info("Received request to create tour: {}", request);
        // validation error
        if (result.hasErrors()) {
            log.warn("Validation errors while creating tour: {}", result.getAllErrors());
            model.addAttribute("title", "Create Tour");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("statuses", TourStatus.values());
            model.addAttribute("tour", request);
            return "admin/tours/new";
        }
        try {
            tourService.createTour(request);
            redirectAttributes.addFlashAttribute("successMessage", "Tour created successfully!");
            return "redirect:/admin/tours";
        } catch (Exception e) {
            log.error("Error occurred while creating tour: {}", e.getMessage());
            model.addAttribute("title", "Create Tour");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("statuses", TourStatus.values());
            model.addAttribute("errorMessage", "Failed to create tour: " + e.getMessage());
            return "admin/tours/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editTour(
            @PathVariable Long id,
            Model model) {
        try {
            EditTourRequest request = tourService.getTourForEdit(id);

            model.addAttribute("tour", request);
            model.addAttribute("tourImages", tourService.getTourImages(id));
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("statuses", TourStatus.values());

            return "admin/tours/edit";
        } catch (Exception e) {
            log.error("Error occurred while fetching tour for edit: {}", e.getMessage());
            model.addAttribute("errorMessage", "Failed to fetch tour for edit: " + e.getMessage());
            return "redirect:/admin/tours";
        }
    }

    @PutMapping("/{id}")
    public String updateTour(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("tour") EditTourRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Received request to update tour: {}", id);

        if (result.hasErrors()) {
            model.addAttribute("title", "Edit Tour");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("statuses", TourStatus.values());
            model.addAttribute("tourImages", tourService.getTourImages(id));

            return "admin/tours/edit";
        }

        try {

            tourService.updateTour(id, request);

            redirectAttributes.addFlashAttribute("successMessage", "Tour updated successfully!");

            return "redirect:/admin/tours";

        } catch (Exception e) {
            log.error("Error updating tour {}", id, e);

            model.addAttribute("title", "Edit Tour");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("statuses", TourStatus.values());
            model.addAttribute("tourImages", tourService.getTourImages(id));
            model.addAttribute("errorMessage", "Failed to update tour: " + e.getMessage());
            return "admin/tours/edit";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteTour(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            tourService.deleteTour(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Tour deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete failed: " + e.getMessage());
        }

        return "redirect:/admin/tours";
    }
}
