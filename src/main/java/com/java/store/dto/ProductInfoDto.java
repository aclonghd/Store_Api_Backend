package com.java.store.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProductInfoDto {
    private Long id;
    private String title;
    private float price;
    private int quantity;
    private String color;
    private int memory;
    private String mainImage;
    private float averageRatting;
    private int voteNumber;
    private Set<String> tags;
}
