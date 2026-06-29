package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.response.DashboardResponse;
import com.bookingtour.sun.repository.BookingRepository;
import com.bookingtour.sun.repository.TourRepository;
import com.bookingtour.sun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final BookingRepository bookingRepository;
    public DashboardResponse getDashboard() {
        return DashboardResponse.builder()
                .totalUsers(userRepository.countUsers())
                .totalTours(tourRepository.count())
                .totalBookings(bookingRepository.count())
                .revenue(bookingRepository.getRevenue())
                .build();
    }
}
