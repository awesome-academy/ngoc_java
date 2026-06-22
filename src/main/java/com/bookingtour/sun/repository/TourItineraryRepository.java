package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.TourItinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TourItineraryRepository extends JpaRepository<TourItinerary, Long> {
    List<TourItinerary> findByTourId(Long tourId);
}
