package com.bookingtour.sun.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Integer rating;
    private String comment;
}
