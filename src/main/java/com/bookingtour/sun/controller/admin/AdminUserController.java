package com.bookingtour.sun.controller.admin;

import com.bookingtour.sun.dto.request.*;
import com.bookingtour.sun.dto.response.UserResponse;
import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import com.bookingtour.sun.services.CurrentUserService;
import com.bookingtour.sun.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService userService;
    private final CurrentUserService currentUserService;

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

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("activeMenu", "users");
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("user", new AdminCreateUserRequest());
        return "admin/users/new";
    }

    @PostMapping()
    public String createUser(
            @Valid @ModelAttribute("user") AdminCreateUserRequest request,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match.");
            model.addAttribute("statuses", UserStatus.values());
            model.addAttribute("roles", UserRole.values());
            return "admin/users/new";
        }

        if (result.hasErrors()) {
            model.addAttribute("statuses", UserStatus.values());
            model.addAttribute("roles", UserRole.values());
            return "admin/users/new";
        }

        userService.createUser(request);
        redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activeMenu", "users");
        try {
            AdminUpdateUserRequeset request = userService.getForUpdate(id);
            model.addAttribute("statuses", UserStatus.values());
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("user", request);
            return "admin/users/edit";
        } catch (Exception e) {
            log.error("Error occurred while fetching user for edit: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("user") AdminUpdateUserRequeset request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!StringUtils.hasText(request.getPassword())) {

            request.setPassword(null);
            request.setConfirmPassword(null);

        } else {
            if (request.getPassword().length() < 8
                    || request.getPassword().length() > 100) {

                result.rejectValue(
                        "password",
                        "password.length",
                        "Password must be between 8 and 100 characters."
                );
            }
            if (!StringUtils.hasText(request.getConfirmPassword())) {

                result.rejectValue(
                        "confirmPassword",
                        "confirmPassword.required",
                        "Please confirm your password."
                );
            } else if (!request.getPassword().equals(request.getConfirmPassword())) {

                result.rejectValue(
                        "confirmPassword",
                        "password.mismatch",
                        "Password confirmation does not match."
                );
            }
        }

        if (result.hasErrors()) {

            model.addAttribute("roles", UserRole.values());
            model.addAttribute("statuses", UserStatus.values());

            return "admin/users/edit";
        }

        userService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            String email = currentUserService.getCurrentUserEmail();
            userService.deleteUser(id, email);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete failed: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}
