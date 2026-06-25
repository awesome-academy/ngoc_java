package com.bookingtour.sun.dto.response;

import com.bookingtour.sun.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingReponse {
    private Long id;
    private Long tourId;
    private String tourName;
    private String imageUrl;
    private LocalDate startDate;
    private Integer numberOfPeople;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDate bookingDate;
    private String note;
}
