package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.services.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminDashboardService dashboardService;

    @GetMapping()
    public String dashboard(Model model) {

        model.addAttribute("title", "Dashboard");
        model.addAttribute("activeMenu", "home");
        model.addAttribute("dashboard", dashboardService.getDashboard());
        return "admin/dashboard";
    }
}
