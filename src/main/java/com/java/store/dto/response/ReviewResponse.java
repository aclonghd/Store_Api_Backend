package com.java.store.dto.response;

import com.java.store.dto.ProductDto;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ReviewResponse {
    private Long id;
    private UserResponse user;
    private ProductDto product;
    private float reviewScore;
    private Timestamp timeStamp;
    private String review;
    private List<ReviewResponse> reply;
}
