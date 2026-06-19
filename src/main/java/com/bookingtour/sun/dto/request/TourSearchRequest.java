package com.bookingtour.sun.dto.request;

import com.bookingtour.sun.enums.TourStatus;
import lombok.Data;

@Data
public class TourSearchRequest {
    private String name;
    private Long categoryId;
    private TourStatus status;
    private Integer page = 0;
    private Integer size = 10;
}
