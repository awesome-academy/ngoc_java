package com.bookingtour.sun.entity;

import com.bookingtour.sun.enums.TourStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import com.bookingtour.sun.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tours")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tour extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "departure_location", length = 255)
    private String departureLocation;

    @Column(length = 255)
    private String destination;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "max_people")
    private Integer maxPeople;

    @Column(precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 500)
    private String thumbnail;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Builder.Default
    @Column(name = "total_rating")
    private Integer totalRating = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TourStatus status;
}
