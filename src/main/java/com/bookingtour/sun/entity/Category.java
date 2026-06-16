package com.bookingtour.sun.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT", length = 1000)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
