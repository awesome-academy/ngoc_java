package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.*;
import com.bookingtour.sun.dto.response.TourImageResponse;
import com.bookingtour.sun.dto.response.TourItineraryResponse;
import com.bookingtour.sun.dto.response.TourResponse;
import com.bookingtour.sun.entity.Category;
import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.entity.TourImage;
import com.bookingtour.sun.entity.TourItinerary;
import com.bookingtour.sun.enums.TourStatus;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.repository.*;
import com.bookingtour.sun.specification.TourSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {
    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;
    private final FileStorageService fileStorageService;
    private final TourItineraryRepository tourItineraryRepository;
    private final TourReviewRepository tourReviewRepository;
    private final BookingRepository bookingRepository;

    @Transactional
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
        // First persist the tour to obtain an id so TourImage can reference it
        Tour savedTour = tourRepository.save(tour);
        List<TourImage> images = new ArrayList<>();

        if (request.getImages() != null) {
            for (MultipartFile file : request.getImages()) {

                if (file == null || file.isEmpty()) {
                    continue;
                }

                String imageUrl = fileStorageService.storeFile(file);

                images.add(TourImage.builder()
                                .tour(savedTour)
                                .imageUrl(imageUrl)
                                .build()
                );
            }
        }

        if (!images.isEmpty()) {
            tourImageRepository.saveAll(images);
            log.info("Tour Image created successfully with tour id: {}", savedTour.getId());
        }
        for (CreateTourItineraryRequest item : request.getItineraries()) {
            log.info("Creating itinerary for tour id: {}, day: {}, description: {}", savedTour.getId(), item.getDayNumber(), item.getDescription());
            TourItinerary itinerary = new TourItinerary();

            itinerary.setTour(savedTour);
            itinerary.setDayNumber(item.getDayNumber());
            itinerary.setTitle(item.getTitle());
            itinerary.setDescription(item.getDescription());

            tourItineraryRepository.save(itinerary);
        }
        log.info("Tour created successfully with id: {}", savedTour.getId());
        return mapToResponse(savedTour);
    }

    public PageResponse<TourResponse> searchTours(TourSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Specification<Tour> specification = TourSpecification.filter(request.getName(),
                request.getCategoryId(),
                request.getStatus(),
                null, null
        );
        Page<Tour> page = tourRepository.findAll(specification, pageable);

        List<TourResponse> tours = page.getContent()
                                        .stream()
                                        .map(this::mapToResponse)
                                        .toList();
        return PageResponse.<TourResponse>builder()
                .content(tours)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public PageResponse<TourResponse> searchClientTours(ClientTourSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Specification<Tour> specification = TourSpecification.filter(request.getName(),
                request.getCategoryId(),
                TourStatus.ACTIVE,
                request.getStartDate(),
                request.getMaxPrice()
        );
        Page<Tour> page = tourRepository.findAll(specification, pageable);

        List<TourResponse> tours = page.getContent()
                                        .stream()
                                        .map(this::mapToResponse)
                                        .toList();
        return PageResponse.<TourResponse>builder()
                .content(tours)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public List<TourResponse> clientTours() {
        List<Tour> topTours = tourRepository.findTop4ByStatusOrderByAverageRatingDesc(
                TourStatus.ACTIVE
        );
        return topTours.stream().map(this::mapToResponse).toList();
    }

    public EditTourRequest getTourForEdit(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + id));

        EditTourRequest request = new EditTourRequest();

        request.setId(tour.getId());
        request.setName(tour.getName());
        request.setDescription(tour.getDescription());
        request.setDepartureLocation(tour.getDepartureLocation());
        request.setDestination(tour.getDestination());
        request.setPrice(tour.getPrice());
        request.setMaxPeople(tour.getMaxPeople());
        request.setDurationDays(tour.getDurationDays());
        request.setStartDate(tour.getStartDate());
        request.setEndDate(tour.getEndDate());
        request.setStatus(tour.getStatus());
        request.setCategoryName(tour.getCategory() != null ? tour.getCategory().getName() : null);

        if (tour.getCategory() != null) {
            request.setCategoryId(tour.getCategory().getId());
        }

        List<EditTourItineraryRequest> itineraries =
                tour.getItineraries()
                        .stream()
                        .map(item -> EditTourItineraryRequest.builder()
                                .id(item.getId())
                                .dayNumber(item.getDayNumber())
                                .title(item.getTitle())
                                .description(item.getDescription())
                                .build())
                        .toList();
        request.setItineraries(itineraries);
        RatingSummary rating = tourReviewRepository.getRatingSummary(id);
        if(rating != null){
            request.setAverageRating(rating.getAverageRating());
            request.setReviewCount(rating.getReviewCount());
        }
        else {
            request.setAverageRating(0D);
            request.setReviewCount(0L);
        }

        return request;
    }

    public List<TourImageResponse> getTourImages(Long tourId) {

        return tourImageRepository
                .findByTourId(tourId)
                .stream()
                .map(image -> TourImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .toList();
    }

    public List<TourItineraryResponse> getTourItineraries(Long tourId) {
        return tourItineraryRepository
                .findByTourId(tourId)
                .stream()
                .sorted((a, b) -> Integer.compare(a.getDayNumber(), b.getDayNumber()))
                .map(itinerary -> TourItineraryResponse.builder()
                        .id(itinerary.getId())
                        .dayNumber(itinerary.getDayNumber())
                        .title(itinerary.getTitle())
                        .description(itinerary.getDescription())
                        .build())
                .toList();
    }

    @Transactional
    public void updateTour(Long id, EditTourRequest request) {

        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + id));

        // update fields
        tour.setName(request.getName());
        tour.setDescription(request.getDescription());
        tour.setDepartureLocation(request.getDepartureLocation());
        tour.setDestination(request.getDestination());
        tour.setPrice(request.getPrice());
        tour.setMaxPeople(request.getMaxPeople());
        tour.setDurationDays(request.getDurationDays());
        tour.setStartDate(request.getStartDate());
        tour.setEndDate(request.getEndDate());
        tour.setStatus(request.getStatus());

        // delete images
        if(request.getDeletedImageIds() != null){
            log.info("deletedImageIds={}", request.getDeletedImageIds());

            List<TourImage> images = tourImageRepository.findAllById(request.getDeletedImageIds())
                    .stream()
                    .filter(image ->  image.getTour() != null && image.getTour().getId().equals(id))
                    .toList();;

            for(TourImage image : images){
                fileStorageService.delete(image.getImageUrl());
            }

            tourImageRepository.deleteAll(images);
        }

        // add new images
        if(request.getNewImages() != null  && !request.getNewImages().isEmpty()){

            for(MultipartFile file : request.getNewImages()){
                if (file == null || file.isEmpty()) {
                    continue;
                }
                log.info("Adding new image for tour id={}", id);
                String url = fileStorageService.storeFile(file);

                TourImage image = TourImage.builder()
                                .tour(tour)
                                .imageUrl(url)
                                .build();

                tourImageRepository.save(image);
            }
        }
        tour.getItineraries().clear();
        for (EditTourItineraryRequest item : request.getItineraries()) {

            TourItinerary itinerary = new TourItinerary();

            itinerary.setTour(tour);
            itinerary.setDayNumber(item.getDayNumber());
            itinerary.setTitle(item.getTitle());
            itinerary.setDescription(item.getDescription());
            tour.getItineraries().add(itinerary);
            log.info("Adding itinerary for tour id={}, dayNumber={}", id, item.getDayNumber());
        }

        tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long id) {

        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + id));
        if(bookingRepository.existsByTourId(id)){
            throw new IllegalStateException("Cannot delete tour because it has bookings");
        }

        List<TourImage> images = tourImageRepository.findByTourId(id);

        // Xóa file ảnh
        for (TourImage image : images) {
            fileStorageService.delete(image.getImageUrl());
        }

        // Xóa record ảnh
        tourImageRepository.deleteAll(images);

        // Xóa tour
        tourRepository.delete(tour);
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
                .categoryName(tour.getCategory() != null ? tour.getCategory().getName() : "N/A")
                .status(tour.getStatus())
                .imageUrl(tour.getImages() != null && !tour.getImages().isEmpty() ? tour.getImages().get(0).getImageUrl() : null)
                .build();
    }
}
