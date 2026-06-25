package com.bookingtour.sun.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {
    private long totalUsers;
    private long totalTours;
    private long totalBookings;
    private BigDecimal revenue;
}
