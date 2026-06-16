package com.bookingtour.sun.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/tours")
public class AdminTourController {

    @GetMapping("/new")
    public String createTour(Model model) {
        model.addAttribute("title", "Create Tour");
        // TODO: load categories from DB and add to model
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
}
