package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment,Long> {
    Optional<Payment> findByBookingId(Long bookingId);
}
