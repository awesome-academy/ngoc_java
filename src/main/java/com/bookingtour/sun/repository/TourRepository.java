package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
