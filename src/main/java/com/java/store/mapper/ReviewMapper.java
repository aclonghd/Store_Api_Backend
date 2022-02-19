package com.java.store.mapper;

import com.java.store.dto.NewReviewDto;
import com.java.store.dto.ReviewDto;
import com.java.store.module.Product;
import com.java.store.module.Review;
import com.java.store.module.Users;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewMapper implements BaseMapper<ReviewDto, Review> {
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @Override
    public Review DtoToEntity(ReviewDto reviewDto) {
        return null;
    }

    @Override
    public ReviewDto EntityToDto(Review review) {
        ReviewDto res = new ReviewDto();
        res.setId(review.getId());
        res.setReviewScore(review.getReviewScore());
        res.setReview(review.getReview());
        res.setUser(userMapper.EntityToDto(review.getUser()));
        res.setProduct(productMapper.EntityToInfoDto(review.getProduct()));
        return res;
    }

    public Review NewDtoToEntity(NewReviewDto reviewDto, Users user, Product product){
        Review res = new Review();
        res.setReview(reviewDto.getReview());
        res.setReviewScore(reviewDto.getReviewScore());
        res.setUser(user);
        res.setProduct(product);
        res.setParentId(reviewDto.getParentId());
        return res;
    }
}
