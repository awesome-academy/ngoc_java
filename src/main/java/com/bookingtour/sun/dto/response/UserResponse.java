package com.bookingtour.sun.dto.response;

import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private UserStatus status;
}
