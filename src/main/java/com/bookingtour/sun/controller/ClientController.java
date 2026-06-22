package com.bookingtour.sun.controller;
import com.bookingtour.sun.dto.response.TourResponse;
import com.bookingtour.sun.services.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final TourService tourService;

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
}
