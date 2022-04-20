package com.java.store.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ReviewDto {
    private Long id;
    private UserDto user;
    private ProductInfoDto product;
    private float reviewScore;
    private Timestamp timeStamp;
    private String review;
    private List<ReviewDto> reply;
}
