package com.java.store.mapper;

import com.java.store.dto.ProductDto;
import com.java.store.module.Product;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductMapper implements BaseMapper<ProductDto, Product>{
    @Override
    public Product DtoToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setColor(productDto.getColor());
        product.setInformation(productDto.getInformation());
        product.setQuantity(productDto.getQuantity());
        product.setImageUrl(productDto.getImageUrl());
        product.setMainImage(productDto.getMainImage());
        product.setVoteNumber(productDto.getVoteNumber());
        product.setTotalRatingScore(productDto.getTotalRatingScore());
        product.setTitleUrl(productDto.getTitleUrl());
        product.setProductTag(productDto.getProductTag());
        product.setSpecifications(productDto.getSpecifications());
        product.setMemory(productDto.getMemory());
        return product;
    }

    @Override
    public ProductDto EntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setColor(product.getColor());
        productDto.setPrice(product.getPrice());
        productDto.setTitle(product.getTitle());
        productDto.setInformation(product.getInformation());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setQuantity(product.getQuantity());
        productDto.setVoteNumber(product.getVoteNumber());
        productDto.setTotalRatingScore(product.getTotalRatingScore());
        productDto.setMainImage(product.getMainImage());
        productDto.setTitleUrl(product.getTitleUrl());
        productDto.setProductTag(product.getProductTag());
        productDto.setSpecifications(product.getSpecifications());
        productDto.setMemory(product.getMemory());
        Set<String> tags = new HashSet<>();
        product.getTags().forEach(tag -> tags.add(tag.getTitle()));
        productDto.setTags(tags);
        return productDto;
    }
}
