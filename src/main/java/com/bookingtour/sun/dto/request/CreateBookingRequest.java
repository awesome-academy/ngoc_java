package com.bookingtour.sun.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequest {
    @NotNull
    private Long tourId;

    @NotNull
    @Min(value = 1, message = "Number of people must be at least 1")
    private Integer numberOfPeople;

    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    private String note;

    @NotNull
    private LocalDate bookingDate;
}
