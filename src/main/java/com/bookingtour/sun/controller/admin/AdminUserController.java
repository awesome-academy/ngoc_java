package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.request.UserSearchRequest;
import com.bookingtour.sun.dto.response.UserResponse;
import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import com.bookingtour.sun.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService userService;

    @GetMapping()
    public String listUser(@ModelAttribute UserSearchRequest request, Model model) {
        model.addAttribute("title", "Manage Users");
        model.addAttribute("activeMenu", "users");
        PageResponse<UserResponse> pageResponse = userService.searchUsers(request);

        model.addAttribute("users", pageResponse.getContent());
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("roles", UserRole.values());
        // Pagination and filter info for template
        model.addAttribute("pageNumber", pageResponse.getPage() + 1);
        model.addAttribute("pageSize", pageResponse.getSize());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("currentKeyword", request.getKeyword() != null ? request.getKeyword() : "");
        model.addAttribute("currentStatus", request.getStatus() != null ? request.getStatus() : "");
        model.addAttribute("currentRole", request.getRole() != null ? request.getRole() : "");
        return "admin/users/index";
    }
}
