package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.BookingSearchRequest;
import com.bookingtour.sun.dto.request.CreateBookingRequest;
import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.response.BookingReponse;
import com.bookingtour.sun.entity.Booking;
import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.entity.TourImage;
import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.repository.BookingRepository;
import com.bookingtour.sun.repository.TourRepository;
import com.bookingtour.sun.repository.UserRepository;
import com.bookingtour.sun.specification.BookingSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final BookingRepository bookingRepository;

    public void createBooking(String email, CreateBookingRequest request) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Tour tour = tourRepository
                .findById(request.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + request.getTourId()));
        BigDecimal totalAmount = tour.getPrice().multiply(BigDecimal.valueOf(request.getNumberOfPeople()));
        Booking booking = Booking.builder()
                .user(user)
                .tour(tour)
                .numberOfPeople(request.getNumberOfPeople())
                .totalAmount(totalAmount)
                .note(request.getNote())
                .bookingDate(request.getBookingDate())
                .build();
        bookingRepository.save(booking);
        log.info("Booking created successfully for user: {} and tour: {}", email, tour.getName());
    }
    public PageResponse<BookingReponse> getMyBooking(String email, BookingSearchRequest request) {
        log.info("Search tour status: {}", request.getStatus());
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(
                        Sort.Direction.DESC,
                        "bookingDate")
        );

        Page<Booking> bookings =
                bookingRepository.findAll(
                        BookingSpecification.filter(
                                user.getId(),
                                request.getStatus()),
                        pageable);
        List<BookingReponse> content =
                bookings.getContent()
                        .stream()
                        .map(this::toBookingResponse)
                        .toList();
        return PageResponse.<BookingReponse>builder()
                .content(content)
                .page(bookings.getNumber())
                .size(bookings.getSize())
                .totalElements(bookings.getTotalElements())
                .totalPages(bookings.getTotalPages())
                .build();
    }

    private BookingReponse toBookingResponse(
            Booking booking) {

        String imageUrl = booking.getTour()
                        .getImages()
                        .stream()
                        .findFirst()
                        .map(TourImage::getImageUrl)
                        .orElse("/uploads/tours/default-tour.jpg");

        return BookingReponse.builder()
                .id(booking.getId())
                .tourId(booking.getTour().getId())
                .tourName(booking.getTour().getName())
                .imageUrl(imageUrl)
                .startDate(booking.getTour().getStartDate())
                .numberOfPeople(booking.getNumberOfPeople())
                .totalAmount(booking.getTotalAmount())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .build();
    }
}
