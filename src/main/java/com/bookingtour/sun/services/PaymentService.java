package com.bookingtour.sun.services;

import com.bookingtour.sun.entity.Booking;
import com.bookingtour.sun.entity.Payment;
import com.bookingtour.sun.enums.BookingStatus;
import com.bookingtour.sun.enums.PaymentStatus;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.exception.UnauthorizedException;
import com.bookingtour.sun.repository.BookingRepository;
import com.bookingtour.sun.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public String createPayment(Long bookingId, String email){
        Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if(!booking.getUser().getEmail().equals(email)){
            throw new UnauthorizedException("Invalid booking");
        }
        if(booking.getStatus() != BookingStatus.PENDING_PAYMENT){
            throw new IllegalStateException("Booking cannot be paid");
        }
        Payment payment = paymentRepository
                .findByBookingId(bookingId)
                .orElse(null);
        if (payment == null) {
             payment = Payment.builder()
                    .booking(booking)
                    .amount(booking.getTotalAmount())
                    .status(PaymentStatus.PENDING)
                    .paymentMethod("ONLINE")
                    .build();
        }

        paymentRepository.save(payment);

        // gia lap url
        return "/payments/mock/" + payment.getId();
    }

    @Transactional
    public void success(Long paymentId,  String email){
        Payment payment = paymentRepository.findById(paymentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        if(!payment.getBooking().getUser().getEmail().equals(email)){
            throw new UnauthorizedException("Invalid booking");
        }
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.getBooking().setStatus(BookingStatus.PAID);
    }
}
