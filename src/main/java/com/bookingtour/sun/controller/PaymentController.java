package com.bookingtour.sun.controller;

import com.bookingtour.sun.entity.Payment;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.exception.UnauthorizedException;
import com.bookingtour.sun.repository.PaymentRepository;
import com.bookingtour.sun.services.CurrentUserService;
import com.bookingtour.sun.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;
    private final PaymentRepository paymentRepository;


    @PostMapping("/{bookingId}/create")
    public String createPayment(@PathVariable Long bookingId, RedirectAttributes redirectAttributes)
    {
        try {
            String email = currentUserService.getCurrentUserEmail();
            String url = paymentService.createPayment(bookingId, email);
            return "redirect:" + url;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại");
            return "redirect:/bookings";
        }
    }

    @GetMapping("/mock/{id}")
    public String mockPayment(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        try {
            String email = currentUserService.getCurrentUserEmail();
            Payment payment = paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
            if(!payment.getBooking().getUser().getEmail().equals(email)) {
                throw new UnauthorizedException("Invalid booking");
            }
            model.addAttribute("paymentId", id);
            return "client/payment/mock";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại");
            return "redirect:/bookings";
        }
    }

    @PostMapping("/{id}/success")
    public String success(@PathVariable Long id, RedirectAttributes redirectAttributes)
    {
        try {
            String email = currentUserService.getCurrentUserEmail();
            paymentService.success(id, email);
            redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại");
        }

        return "redirect:/bookings";
    }
}
