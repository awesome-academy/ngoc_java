package com.bookingtour.sun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RatingSummary {
    private Double averageRating;
    private Long reviewCount;
}
