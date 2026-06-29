package com.bookingtour.sun.controller;

import com.bookingtour.sun.dto.request.ClientTourSearchRequest;
import com.bookingtour.sun.dto.request.EditTourRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.request.ReviewRequest;
import com.bookingtour.sun.dto.response.TourResponse;
import com.bookingtour.sun.services.CategoryService;
import com.bookingtour.sun.services.CurrentUserService;
import com.bookingtour.sun.services.TourReviewService;
import com.bookingtour.sun.services.TourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tours")
@AllArgsConstructor
@Slf4j
public class TourController {
    private final CategoryService categoryService;
    private final TourService tourService;
    private final TourReviewService reviewService;
    private final CurrentUserService currentUserService;

    @GetMapping("")
    public String listTour(@ModelAttribute ClientTourSearchRequest searchForm, Model model) {
        model.addAttribute("title", "List Tours");
        model.addAttribute("categories", categoryService.getAllCategories());
        PageResponse<TourResponse> pageResponse = tourService.searchClientTours(searchForm);
        model.addAttribute("tours", pageResponse.getContent());

        // Pagination and filter info for template
        model.addAttribute("pageNumber", pageResponse.getPage() + 1);
        model.addAttribute("pageSize", pageResponse.getSize());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("currentSearch", searchForm.getName() != null ? searchForm.getName() : "");
        model.addAttribute("currentCategory", searchForm.getCategoryId() != null ? searchForm.getCategoryId() : "");
        model.addAttribute("currentMaxPrice",  searchForm.getMaxPrice() != null ? searchForm.getMaxPrice() : "");
        model.addAttribute("currentStartDate", searchForm.getStartDate() != null ? searchForm.getStartDate() : "");
        return "client/tours/index";
    }

    @GetMapping("/{id}")
    public String showTour(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("title", "Tour detail");
        try {
            EditTourRequest request = tourService.getTourForEdit(id);

            model.addAttribute("tour", request);
            model.addAttribute("tourImages", tourService.getTourImages(id));
            model.addAttribute("tourItineraries", tourService.getTourItineraries(id));
            model.addAttribute("reviews", reviewService.getReviews(id));

            return "client/tours/show";
        } catch (Exception e) {
            log.error("Error occurred while fetching tour for edit: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to fetch tour for viewing: " + e.getMessage());
            return "redirect:/tours";
        }
    }

    @PostMapping("/{id}/reviews")
    public String review(
            @PathVariable Long id,
            @ModelAttribute ReviewRequest request,
            RedirectAttributes redirectAttributes){

        try {
            String email = currentUserService.getCurrentUserEmail();
            reviewService.createReview(id, email, request);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to review tour for viewing: " + e.getMessage());
        }

        return "redirect:/tours/"+id;
    }
}
