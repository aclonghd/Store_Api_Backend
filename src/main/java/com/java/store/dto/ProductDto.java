package com.java.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long id;
    private String title;
    private float price;
    private String information;
    private int quantity;
    private int amount;
    private String color;
    private String mainImage;
    private float totalRatingScore;
    private int voteNumber;
    private String productTag;
    private String specifications;
    private int memory;
    private String titleUrl;
    private Set<String> imageUrl;
    private Set<String> tags;
}
