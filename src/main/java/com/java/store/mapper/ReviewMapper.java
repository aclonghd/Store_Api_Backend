package com.java.store.mapper;

import com.java.store.dto.request.NewReviewRequest;
import com.java.store.dto.response.ReviewResponse;
import com.java.store.module.Product;
import com.java.store.module.Review;
import com.java.store.module.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper implements BaseMapper<ReviewResponse, Review> {
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @Autowired
    public ReviewMapper(UserMapper userMapper, ProductMapper productMapper) {
        this.userMapper = userMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Review DtoToEntity(ReviewResponse reviewResponse) {
        return null;
    }

    @Override
    public ReviewResponse EntityToDto(Review review) {
        ReviewResponse res = new ReviewResponse();
        res.setId(review.getId());
        res.setReviewScore(review.getReviewScore());
        res.setReview(review.getReview());
        res.setUser(userMapper.EntityToDto(review.getUser()));
        res.setProduct(productMapper.EntityToDto(review.getProduct()));
        res.setTimeStamp(review.getTimeStamp());
        return res;
    }

    public Review NewDtoToEntity(NewReviewRequest reviewDto, Users user, Product product, Review reviewParent){
        Review res = new Review();
        res.setReview(reviewDto.getReview());
        res.setReviewScore(reviewDto.getReviewScore());
        res.setUser(user);
        res.setProduct(product);
        res.setReviewParent(reviewParent);
        res.setTimeStamp(reviewDto.getTimeStamp());
        return res;
    }
}
