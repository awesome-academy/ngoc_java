package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.AdminBookingSearchRequest;
import com.bookingtour.sun.dto.request.AdminEditBookingRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.BookingReponse;
import com.bookingtour.sun.enums.BookingStatus;
import com.bookingtour.sun.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
@Slf4j
public class AdminBookingController {
    private final BookingService bookingService;

    @GetMapping()
    public String listAdminBookings(@ModelAttribute AdminBookingSearchRequest request, Model model) {
        model.addAttribute("title", "Manage Bookings");
        model.addAttribute("activeMenu", "bookings");
        PageResponse<BookingReponse> pageResponse = bookingService.getAdminBookings(request);
        model.addAttribute("bookings", pageResponse.getContent());
        model.addAttribute("statuses", BookingStatus.values());

        // Pagination and filter info for template
        model.addAttribute("pageNumber", pageResponse.getPage() + 1);
        model.addAttribute("pageSize", pageResponse.getSize());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("currentKeyword", request.getKeyword());
        model.addAttribute("currentStatus", request.getStatus());

        return "admin/booking/index";
    }

    @GetMapping("/{id}/edit")
    public String editBooking(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes) {
        model.addAttribute("activeMenu", "bookings");
        try {
            AdminEditBookingRequest request = bookingService.getBookingForEdit(id);
            model.addAttribute("booking", request);
            model.addAttribute("statuses", BookingStatus.values());
            return "admin/booking/edit";

        } catch (Exception e) {
            log.error("Error occurred while fetching booking for edit: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to fetch booking for edit: " + e.getMessage());
            return "redirect:/admin/bookings";
        }
    }

    @PutMapping("/{id}")
    public String updateBooking(@PathVariable Long id, @Valid @ModelAttribute("booking") AdminEditBookingRequest request,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Edit Booking");
            model.addAttribute("statuses", BookingStatus.values());
            return "admin/booking/edit";
        }

        try {
            bookingService.updateBooking(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Booking updated successfully!");
            return "redirect:/admin/bookings";

        } catch (Exception e) {
            log.error("Error updating booking {}", id, e);
            model.addAttribute("title", "Edit Booking");
            model.addAttribute("statuses", BookingStatus.values());
            return "admin/booking/edit";
        }
    }
}
