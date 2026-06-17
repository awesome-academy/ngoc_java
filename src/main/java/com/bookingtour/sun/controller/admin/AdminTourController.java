package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.CreateTourRequest;
import com.bookingtour.sun.enums.TourStatus;
import com.bookingtour.sun.services.CategoryService;
import com.bookingtour.sun.services.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String listTours(Model model) {
        model.addAttribute("title", "Manage Tours");

        // Sample placeholder tours - replace with real service/repository later
        List<Map<String, Object>> tours = new ArrayList<>();
        Map<String, Object> t1 = new HashMap<>();
        t1.put("id", 1);
        t1.put("title", "Halong Bay Cruise");
        t1.put("categoryName", "Beach");
        t1.put("price", 1500000);
        t1.put("seats", 20);
        t1.put("startDate", "2026-06-15");
        t1.put("published", true);
        tours.add(t1);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("id", 2);
        t2.put("title", "Sapa Trekking");
        t2.put("categoryName", "Mountain");
        t2.put("price", 1200000);
        t2.put("seats", 15);
        t2.put("startDate", "2026-07-01");
        t2.put("published", false);
        tours.add(t2);

        model.addAttribute("tours", tours);
        model.addAttribute("pageNumber", 1);

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
            return "admin/tours/new";
        }
        try {
            tourService.createTour(request);
            redirectAttributes.addFlashAttribute("successMessage", "Tour created successfully!");
            return "redirect:/admin/tours";
        } catch (Exception e) {
            log.error("Error occurred while creating tour: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create tour.");
            return "admin/tours/new";
        }
    }
}
