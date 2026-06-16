package com.bookingtour.sun.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_images")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
}
