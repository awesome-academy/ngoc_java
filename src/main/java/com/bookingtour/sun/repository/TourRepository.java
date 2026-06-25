package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.enums.TourStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {
    List<Tour> findTop4ByStatusOrderByAverageRatingDesc(
            TourStatus status
    );

    long count();
}
