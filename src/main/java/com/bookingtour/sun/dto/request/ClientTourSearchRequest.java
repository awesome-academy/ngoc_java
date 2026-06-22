package com.bookingtour.sun.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClientTourSearchRequest {
    private String name;
    private Long categoryId;
    private LocalDate startDate;
    private BigDecimal maxPrice;

    private Integer page = 0;
    private Integer size = 10;
}
