package com.bookingtour.sun.dto.response;

import com.bookingtour.sun.enums.TourStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourResponse {
    private Long id;
    private String name;
    private String description;
    private String departureLocation;
    private String destination;
    private Integer durationDays;
    private Integer maxPeople;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate  endDate;
    private Long categoryId;
    private String categoryName;
    private TourStatus status;
    private String imageUrl;
    private List<TourItineraryResponse> itineraries;
}
