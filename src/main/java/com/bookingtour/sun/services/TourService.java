package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.CreateTourRequest;
import com.bookingtour.sun.dto.response.TourResponse;
import com.bookingtour.sun.entity.Category;
import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {
    private final TourRepository tourRepository;
    public TourResponse createTour(CreateTourRequest request) {
        log.info("Create tour with name: {}", request.getName());
        Category category = null;
        if (request.getCategoryId() != null) {
            category = new Category();
            category.setId(request.getCategoryId());
        }
        Tour tour = Tour.builder()
                .name(request.getName())
                .description(request.getDescription())
                .departureLocation(request.getDepartureLocation())
                .destination(request.getDestination())
                .durationDays(request.getDurationDays())
                .maxPeople(request.getMaxPeople())
                .price(request.getPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .category(category)
                .status(request.getStatus())
                .build();
        Tour savedTour = tourRepository.save(tour);
        log.info("Tour created successfully with id: {}", savedTour.getId());
        return mapToResponse(savedTour);
    }

    private TourResponse mapToResponse(Tour tour) {
        return TourResponse.builder()
                .id(tour.getId())
                .name(tour.getName())
                .description(tour.getDescription())
                .departureLocation(tour.getDepartureLocation())
                .destination(tour.getDestination())
                .durationDays(tour.getDurationDays())
                .maxPeople(tour.getMaxPeople())
                .price(tour.getPrice())
                .startDate(tour.getStartDate())
                .endDate(tour.getEndDate())
                .categoryId(tour.getCategory() != null ? tour.getCategory().getId() : null)
                .status(tour.getStatus())
                .build();
    }
}
