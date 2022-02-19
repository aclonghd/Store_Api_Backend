package com.java.store.mapper;

import com.java.store.dto.ProductDto;
import com.java.store.dto.ProductInfoDto;
import com.java.store.module.Product;
import com.java.store.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductMapper implements BaseMapper<ProductDto, Product>{
    private final ProductRepo productRepo;
    @Override
    public Product DtoToEntity(ProductDto productDto) {
        return productRepo.findById(productDto.getId()).get();
    }

    @Override
    public ProductDto EntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setColor(product.getColor());
        productDto.setPrice(product.getPrice());
        productDto.setTitle(product.getTitle());
        productDto.setInformation(product.getInformation());
        return productDto;
    }

    public ProductInfoDto EntityToInfoDto(Product product){
        ProductInfoDto productInfoDto = new ProductInfoDto();
        productInfoDto.setPrice(product.getPrice());
        productInfoDto.setColor(product.getColor());
        productInfoDto.setId(product.getId());
        productInfoDto.setQuantity(product.getQuantity());
        productInfoDto.setAverageRatting(product.getAverageRatting());
        productInfoDto.setTitle(product.getTitle());
        productInfoDto.setMainImage(product.getMainImage());
        productInfoDto.setVoteNumber(product.getVoteNumber());
        productInfoDto.setTags(product.getTags());
        return productInfoDto;
    }
}
