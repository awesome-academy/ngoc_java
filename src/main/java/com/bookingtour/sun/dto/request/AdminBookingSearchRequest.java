package com.bookingtour.sun.dto.request;

import com.bookingtour.sun.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBookingSearchRequest {
    private BookingStatus status;
    private String keyword;

    private int page = 0;
    private int size = 10;
}
