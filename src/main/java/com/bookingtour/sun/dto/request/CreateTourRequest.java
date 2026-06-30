package com.bookingtour.sun.dto.request;

import com.bookingtour.sun.enums.TourStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTourRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    private String departureLocation;
    private String destination;

    @Min(value = 1, message = "Duration must be a positive number")
    private Integer durationDays;

    @Min(value = 1, message = "Maximum people must be a positive number")
    private Integer maxPeople;

    private BigDecimal price;

    private LocalDate startDate;
    private LocalDate  endDate;
    private Long categoryId;
    private TourStatus status;
    private List<MultipartFile> images;

    @Builder.Default
    @Valid
    private List<CreateTourItineraryRequest> itineraries = new ArrayList<>();
}
