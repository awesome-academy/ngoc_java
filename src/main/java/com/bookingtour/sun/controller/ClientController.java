package com.bookingtour.sun.controller;
import com.bookingtour.sun.dto.request.RegisterRequest;
import com.bookingtour.sun.dto.response.TourResponse;
import com.bookingtour.sun.services.TourService;
import com.bookingtour.sun.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final TourService tourService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        List<TourResponse> topTours = tourService.clientTours();
        model.addAttribute("topTours", topTours);
        return "client/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login - Tour Booking");
        return "client/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register - Tour Booking");
        model.addAttribute("user", new RegisterRequest());
        return "client/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("user") RegisterRequest request,
            BindingResult result,
            Model model) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match.");
            return "client/register";
        }

        if (result.hasErrors()) {
            return "client/register";
        }

        userService.register(request);

        return "redirect:/login?registered";
    }
}
