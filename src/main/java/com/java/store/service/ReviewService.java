package com.java.store.service;

import com.java.store.dto.request.NewReviewRequest;
import com.java.store.dto.response.ReviewResponse;
import com.java.store.dto.response.UserResponse;
import com.java.store.exception.ServiceException;
import com.java.store.mapper.ReviewMapper;
import com.java.store.mapper.UserMapper;
import com.java.store.module.Product;
import com.java.store.module.Review;
import com.java.store.module.Users;
import com.java.store.repository.ProductRepo;
import com.java.store.repository.ReviewRepo;
import com.java.store.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewMapper reviewMapper;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final ProductRepo productRepo;
    private final PasswordEncoder passwordEncoder;

    public List<ReviewResponse> getAllReview(){
        List<ReviewResponse> res = new ArrayList<>();
        List<Review> reviews = reviewRepo.findAll();
        for(Review review: reviews){
            if(review.getReviewParent() == null) {
                ReviewResponse reviewResponse = reviewMapper.EntityToDto(review);
                res.add(reviewResponse);
            }
        }
        for(ReviewResponse reviewResponse : res){
            List<ReviewResponse> replyRes = new ArrayList<>();
            List<Review> replyList = reviewRepo.findAllByParentId(reviewResponse.getId());
            for (Review reply: replyList) {
                ReviewResponse replyDto = reviewMapper.EntityToDto(reply);
                replyRes.add(replyDto);
            }
            reviewResponse.setReply(replyRes);
        }
        return res;
    }

    public List<ReviewResponse> getReviewByProductId(Long productId){
        if(productRepo.existsById(productId)){
            List<ReviewResponse> res = new ArrayList<>();
            List<Review> reviews = reviewRepo.findAllByProductId(productId);
            for(Review review: reviews){
                if(review.getReviewParent() == null) {
                    ReviewResponse reviewResponse = reviewMapper.EntityToDto(review);
                    res.add(reviewResponse);
                }
            }
            for(ReviewResponse reviewResponse : res){
                List<ReviewResponse> replyRes = new ArrayList<>();
                List<Review> replyList = reviewRepo.findAllByParentId(reviewResponse.getId());
                for (Review reply: replyList) {
                    ReviewResponse replyDto = reviewMapper.EntityToDto(reply);
                    replyRes.add(replyDto);
                }
                reviewResponse.setReply(replyRes);
            }
            return res;
        } throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
    }

    public ReviewResponse getReviewById(Long id){
        if(reviewRepo.existsById(id)){
            ReviewResponse res = reviewMapper.EntityToDto(reviewRepo.getById(id));
            List<ReviewResponse> replyRes = new ArrayList<>();
            List<Review> replyList = reviewRepo.findAllByParentId(res.getId());
            for (Review reply: replyList) {
                ReviewResponse replyDto = reviewMapper.EntityToDto(reply);
                replyRes.add(replyDto);
            }
            res.setReply(replyRes);
            return res;
        } throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
    }

    public void addReview(NewReviewRequest review) {
        if(!productRepo.existsById(review.getProductId())) throw new ServiceException(BAD_REQUEST.value(),"Product does not exist");
        Product product = productRepo.getById(review.getProductId());
        product.setVoteNumber(product.getVoteNumber()+1);
        float totalScore = product.getTotalRatingScore() + review.getReviewScore();
        product.setTotalRatingScore(totalScore);
        Users user;
        if(review.getUser().getUsername() == null || userRepo.findByUsername(review.getUser().getUsername()) == null){
            UserResponse userResponse = review.getUser();
            user = userMapper.DtoToEntity(userResponse);
            user.setUsername(passwordEncoder.encode("username"));
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("USER");
            userRepo.save(user);
            userRepo.flush();
        }
        else user = userRepo.findByUsername(review.getUser().getUsername());
        review.setParentId(null);
        reviewRepo.save(reviewMapper.NewDtoToEntity(review, user, product, null));
    }

    public void addReply(NewReviewRequest review) {
        if(!productRepo.existsById(review.getProductId())) throw new ServiceException(BAD_REQUEST.value(),"Product does not exist");
        if(!reviewRepo.existsById(review.getParentId())) throw new ServiceException(BAD_REQUEST.value(),"parent for this reply not found");
        Review reviewParent = reviewRepo.getById(review.getParentId());
        Product product = productRepo.getById(review.getProductId());
        Users user;
        if(review.getUser().getUsername() == null || userRepo.findByUsername(review.getUser().getUsername()) == null){
            UserResponse userResponse = review.getUser();
            user = userMapper.DtoToEntity(userResponse);
            user.setUsername(passwordEncoder.encode("username"));
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("USER");
            userRepo.save(user);
            userRepo.flush();
        }
        else user = userRepo.findByUsername(review.getUser().getUsername());
        reviewRepo.save(reviewMapper.NewDtoToEntity(review, user, product, reviewParent));
    }
}
