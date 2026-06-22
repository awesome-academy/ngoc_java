package com.bookingtour.sun.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourItineraryResponse {
    private Long id;
    private Integer dayNumber;
    private String title;
    private String description;
}
