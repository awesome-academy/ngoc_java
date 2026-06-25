package com.bookingtour.sun.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CategorySearchRequest {
    private String name;

    private Integer page = 0;
    private Integer size = 10;
}
