package com.bookingtour.sun.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourImageResponse {
    private Long id;
    private String imageUrl;
}
