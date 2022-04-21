package com.java.store.repository;

import com.java.store.module.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @Override
    default Product getById(Long id){
        return findById(id).get();
    };

    Product getByTitleUrl(String titleUrl);

    boolean existsByTitleUrl(String titleUrl);

    List<Product> findAllByProductTag(String productTag);

    @Query(value = "Select distinct product_tag from products p",nativeQuery = true)
    List<String> getAllProductTag();
}
