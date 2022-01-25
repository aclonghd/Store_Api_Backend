package com.java.store.service;

import com.java.store.module.Product;
import com.java.store.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public Product getProduct(Long productId) throws Exception{
        if(productRepo.existsById(productId)){
            return productRepo.getById(productId);
        }
        throw new Exception("Bad Request");
    }

    public void addProduct(Product product){
        productRepo.save(product);
    }

    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }

    public void updateProduct(Product product) throws Exception{
        if(!productRepo.existsById(product.getId())){
            throw new Exception("Bad Request");
        }
        productRepo.save(product);
    }

    public void deleteProduct(Collection<Long> listProductId) throws Exception{
        if(productRepo.findAllById(listProductId).size() != listProductId.size()){
            throw new Exception("Bad Request");
        }
        productRepo.deleteAllById(listProductId);
    }
}
