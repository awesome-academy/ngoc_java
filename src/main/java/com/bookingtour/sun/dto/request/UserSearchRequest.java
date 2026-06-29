package com.bookingtour.sun.dto.request;

import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private String keyword;
    private UserRole role;
    private UserStatus status;
    private Integer page = 0;
    private Integer size = 10;
}
