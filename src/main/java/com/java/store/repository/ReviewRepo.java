package com.java.store.repository;

import com.java.store.module.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    @Query(value = "select coalesce(avg(review_score), 0) from reviews r where r.product_id = :product_id", nativeQuery = true)
    float getProductAverageScore(@Param("product_id") Long productId);

    List<Review> findAllByParentId(Long parentId);
}
