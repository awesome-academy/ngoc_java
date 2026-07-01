package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.ReviewRequest;
import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.entity.TourReview;
import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.exception.DuplicateResourceException;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.repository.TourRepository;
import com.bookingtour.sun.repository.TourReviewRepository;
import com.bookingtour.sun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourReviewService {
    private final TourReviewRepository reviewRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;

    public void createReview(
            Long tourId,
            String email,
            ReviewRequest request
    ){
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if(reviewRepository.existsByTourIdAndUserId(tourId, user.getId())){
            throw new DuplicateResourceException("You already reviewed this tour");
        }
        Tour tour = tourRepository.findById(tourId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + tourId));

        TourReview review = TourReview.builder()
                        .tour(tour)
                        .user(user)
                        .rating(request.getRating())
                        .comment(request.getComment())
                        .build();

        reviewRepository.save(review);
    }


    public List<TourReview> getReviews(Long tourId){
        return reviewRepository.findByTourIdOrderByCreatedAtDesc(tourId);
    }
}
