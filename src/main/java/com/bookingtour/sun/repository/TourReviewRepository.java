package com.bookingtour.sun.repository;

import com.bookingtour.sun.dto.request.RatingSummary;
import com.bookingtour.sun.entity.TourReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourReviewRepository
        extends JpaRepository<TourReview,Long> {

    List<TourReview> findByTourIdOrderByCreatedAtDesc(Long tourId);

    boolean existsByTourIdAndUserId(
            Long tourId,
            Long userId
    );

    @Query("""
        SELECT 
            AVG(r.rating),
            COUNT(r.id)
        FROM TourReview r
        WHERE r.tour.id = :tourId
    """)
    RatingSummary getRatingSummary(@Param("tourId") Long tourId);
}
