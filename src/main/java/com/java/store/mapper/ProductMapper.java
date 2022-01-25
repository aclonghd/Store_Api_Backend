package com.java.store.mapper;

import com.java.store.dto.ProductDto;
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
}
