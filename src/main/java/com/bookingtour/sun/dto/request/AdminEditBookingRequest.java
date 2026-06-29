package com.bookingtour.sun.dto.request;

import com.bookingtour.sun.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminEditBookingRequest {
    private Long id;
    private String tourName;
    private String userName;
    private BookingStatus status;
    private String note;
    private LocalDate bookingDate;
}
