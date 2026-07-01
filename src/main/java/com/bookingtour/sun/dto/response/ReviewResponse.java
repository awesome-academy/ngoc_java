package com.bookingtour.sun.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {

    private String username;

    private String avatar;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;
}
