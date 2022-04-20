package com.java.store.repository;

import com.java.store.module.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @Override
    default Product getById(Long id){
        return findById(id).get();
    };

    Product getByTitleUrl(String titleUrl);

    boolean existsByTitleUrl(String titleUrl);
}
