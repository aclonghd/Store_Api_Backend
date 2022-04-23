package com.java.store.service;

import com.java.store.dto.NewReviewDto;
import com.java.store.dto.ReviewDto;
import com.java.store.dto.UserDto;
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

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewMapper reviewMapper;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final ProductRepo productRepo;
    private final PasswordEncoder passwordEncoder;

    public List<ReviewDto> getAllReview(){
        List<ReviewDto> res = new ArrayList<>();
        List<Review> reviews = reviewRepo.findAll();
        for(Review review: reviews){
            if(review.getReviewParent() == null) {
                ReviewDto reviewDto = reviewMapper.EntityToDto(review);
                res.add(reviewDto);
            }
        }
        for(ReviewDto reviewDto: res){
            List<ReviewDto> replyRes = new ArrayList<>();
            List<Review> replyList = reviewRepo.findAllByParentId(reviewDto.getId());
            for (Review reply: replyList) {
                ReviewDto replyDto = reviewMapper.EntityToDto(reply);
                replyRes.add(replyDto);
            }
            reviewDto.setReply(replyRes);
        }
        return res;
    }

    public List<ReviewDto> getReviewByProductId(Long productId) throws Exception{
        if(productRepo.existsById(productId)){
            List<ReviewDto> res = new ArrayList<>();
            List<Review> reviews = reviewRepo.findAllByProductId(productId);
            for(Review review: reviews){
                if(review.getReviewParent() == null) {
                    ReviewDto reviewDto = reviewMapper.EntityToDto(review);
                    res.add(reviewDto);
                }
            }
            for(ReviewDto reviewDto: res){
                List<ReviewDto> replyRes = new ArrayList<>();
                List<Review> replyList = reviewRepo.findAllByParentId(reviewDto.getId());
                for (Review reply: replyList) {
                    ReviewDto replyDto = reviewMapper.EntityToDto(reply);
                    replyRes.add(replyDto);
                }
                reviewDto.setReply(replyRes);
            }
            return res;
        } throw new Exception("Bad request");
    }

    public void addReview(NewReviewDto review) throws Exception{
        if(!productRepo.existsById(review.getProductId())) throw new Exception("Product does not exist");
        Product product = productRepo.getById(review.getProductId());
        product.setVoteNumber(product.getVoteNumber()+1);
        float averageScore = reviewRepo.getProductAverageScore(product.getId());
        if(averageScore != 0)
            product.setAverageRatting((averageScore + review.getReviewScore())/ 2);
        else product.setAverageRatting(review.getReviewScore());
        Users user;
        if(review.getUser().getUsername() == null || userRepo.findByUsername(review.getUser().getUsername()) == null){
            UserDto userDto = review.getUser();
            user = userMapper.DtoToEntity(userDto);
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

    public void addReply(NewReviewDto review) throws Exception{
        if(!productRepo.existsById(review.getProductId())) throw new Exception("Product does not exist");
        if(!reviewRepo.existsById(review.getParentId())) throw new Exception("parent for this reply not found");
        Review reviewParent = reviewRepo.getById(review.getParentId());
        Product product = productRepo.getById(review.getProductId());
        Users user;
        if(review.getUser().getUsername() == null || userRepo.findByUsername(review.getUser().getUsername()) == null){
            UserDto userDto = review.getUser();
            user = userMapper.DtoToEntity(userDto);
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
