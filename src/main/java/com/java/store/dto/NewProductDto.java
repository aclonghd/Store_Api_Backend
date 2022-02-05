package com.java.store.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class NewProductDto {
    private Long id;
    private String title;
    private float price;
    private String information;
    private int quantity;
    private String color;
    private Set<MultipartFile> files;
}
