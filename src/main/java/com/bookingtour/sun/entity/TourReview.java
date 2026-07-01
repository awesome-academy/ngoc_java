package com.bookingtour.sun.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tour_reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uk_user_tour_review",
                        columnNames={"tour_id","user_id"}
                )
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tour_id")
    private Tour tour;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @Column(nullable=false)
    private Integer rating;


    @Column(columnDefinition="TEXT")
    private String comment;
}
