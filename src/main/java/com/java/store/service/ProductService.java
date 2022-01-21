package com.java.store.service;

import com.java.store.module.Product;
import com.java.store.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public Product getProduct(Long productId){
        return productRepo.getById(productId);
    }

    public void addProduct(Product product){
        productRepo.save(product);
    }

    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }
}
