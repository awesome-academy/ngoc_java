package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.*;
import com.bookingtour.sun.dto.response.BookingReponse;
import com.bookingtour.sun.entity.Booking;
import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.entity.TourImage;
import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.enums.BookingStatus;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.exception.UnauthorizedException;
import com.bookingtour.sun.repository.BookingRepository;
import com.bookingtour.sun.repository.TourRepository;
import com.bookingtour.sun.repository.UserRepository;
import com.bookingtour.sun.specification.AdminBookingSpecification;
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

    public PageResponse<BookingReponse> getAdminBookings(AdminBookingSearchRequest request) {
        log.info("Admin search booking for keyword: {}, status: {} ", request.getKeyword(), request.getStatus());
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(
                        Sort.Direction.DESC,
                        "bookingDate")
        );

        Page<Booking> bookings =
                bookingRepository.findAll(
                        AdminBookingSpecification.filter(
                                request.getKeyword(),
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

    public AdminEditBookingRequest getBookingForEdit(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        AdminEditBookingRequest request = new AdminEditBookingRequest();

        request.setId(booking.getId());
        request.setStatus(booking.getStatus());
        request.setUserName(booking.getUser().getUsername());
        request.setTourName(booking.getTour().getName());
        request.setBookingDate(booking.getBookingDate());
        request.setNote(booking.getNote());

        return request;
    }

    public void updateBooking(Long id, AdminEditBookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        booking.setStatus(request.getStatus());
        booking.setNote(request.getNote());
        bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId, String email) {
        Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // check booking owner
        if(!booking.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You cannot cancel this booking");
        }

        // chỉ pending payment mới được cancel
        if(booking.getStatus() != BookingStatus.PENDING_PAYMENT){
            throw new IllegalStateException("Only pending payment booking can be cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
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
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getUsername())
                .build();
    }
}
