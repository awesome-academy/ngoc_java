package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;


public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    long count();

    @Query("""
    SELECT COALESCE(SUM(b.totalAmount),0)
        FROM Booking b
        WHERE b.status  IN ('PAID', 'COMPLETED', 'CONFIRMED')
    """)
    BigDecimal getRevenue();
}

