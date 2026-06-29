package com.bookingtour.sun.controller;

import com.bookingtour.sun.dto.request.BookingSearchRequest;
import com.bookingtour.sun.dto.request.CreateBookingRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.BookingReponse;
import com.bookingtour.sun.services.BookingService;
import com.bookingtour.sun.services.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final CurrentUserService currentUserService;

    @GetMapping("")
    public String listBooking(Model model,
                              BookingSearchRequest request) {
        String email = currentUserService.getCurrentUserEmail();

        PageResponse<BookingReponse> bookings = bookingService.getMyBooking(email, request);
        model.addAttribute("bookings", bookings.getContent());

        // Pagination and filter info for template
        model.addAttribute("pageNumber", bookings.getPage() + 1);
        model.addAttribute("pageSize", bookings.getSize());
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("totalElements", bookings.getTotalElements());
        model.addAttribute("status", request.getStatus());
        return "client/booking/index";
    }

    @PostMapping
    public String createBooking(@AuthenticationPrincipal UserDetails userDetails,
                                 @Valid CreateBookingRequest request,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thông tin đặt tour không hợp lệ");
            return "redirect:/tours/" + request.getTourId();
        }
        try {
            String email = currentUserService.getCurrentUserEmail();
            bookingService.createBooking(email, request);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt tour thành công");
            return "redirect:/bookings";
        } catch (Exception e) {
            log.error("Error occurred while creating booking: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error when booking: " + e.getMessage());
            return "redirect:/tours/" + request.getTourId();
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String email = currentUserService.getCurrentUserEmail();
            bookingService.cancelBooking(id, email);
            redirectAttributes.addFlashAttribute("successMessage", "Hủy đặt tour thành công");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/bookings";
    }
}
