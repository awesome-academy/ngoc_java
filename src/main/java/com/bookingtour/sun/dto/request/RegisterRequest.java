package com.bookingtour.sun.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;

    private String phone;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100,
            message = "Password must be between 8 and 100 characters.")
    private String password;

    @NotBlank(message = "Please confirm your password.")
    private String confirmPassword;
}
