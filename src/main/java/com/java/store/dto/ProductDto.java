package com.java.store.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private float price;
    private String information;
    private int quantity;
    private int amount;
    private String color;
    private String mainImage;
    private float averageRatting;
    private int voteNumber;
    private String productTag;
    private String specifications;
    private int memory;
    private String titleUrl;
    private Set<String> imageUrl;
    private Set<String> tags;
    private Set<MultipartFile> files;
}
