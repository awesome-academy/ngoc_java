package com.bookingtour.sun.dto.request;
import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateUserRequest {
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    private String phone;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100,
            message = "Password must be between 8 and 100 characters.")
    private String password;

    @NotBlank(message = "Please confirm your password.")
    private String confirmPassword;

    @NotNull(message = "Please select a role.")
    private UserRole role;

    @NotNull(message = "Please select a status.")
    private UserStatus status;
}
