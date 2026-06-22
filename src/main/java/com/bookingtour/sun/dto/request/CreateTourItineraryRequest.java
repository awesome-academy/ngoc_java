package com.bookingtour.sun.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTourItineraryRequest {
    @Min(value = 1, message = "Day number must be greater than 0")
    private Integer dayNumber;

    @NotBlank(message = "Itinerary title is required")
    @Size(max = 255, message = "Itinerary title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Itinerary description is required")
    @Size(min = 10, max = 5000,
            message = "Itinerary description must be between 10 and 5000 characters")
    private String description;
}
