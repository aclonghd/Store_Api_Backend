package com.java.store.dto.request;

import com.java.store.dto.response.UserResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class NewReviewRequest {
    @NotNull
    private Long productId;
    @NotNull
    private UserResponse user;
    @NotNull
    private float reviewScore;
    @NotBlank @NotNull
    private String review;
    private Long parentId;
    @NotNull
    private Timestamp timeStamp;
}
