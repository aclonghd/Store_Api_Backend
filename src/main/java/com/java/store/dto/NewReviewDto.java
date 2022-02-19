package com.java.store.dto;

import lombok.Data;

@Data
public class NewReviewDto {
    private Long productId;
    private UserDto user;
    private float reviewScore;
    private String review;
    private Long parentId;
}
