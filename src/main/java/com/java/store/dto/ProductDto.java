package com.java.store.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private float price;
    private String information;
    private int amount;
    private String color;
}
