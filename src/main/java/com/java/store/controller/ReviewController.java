package com.java.store.controller;

import com.java.store.dto.request.NewReviewRequest;
import com.java.store.dto.response.ResponseDto;
import com.java.store.service.ReviewService;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "reviews")
@CrossOrigin("*")
@Validated
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(path = "all-review")
    public ResponseEntity<Object> getAllReview(){
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), reviewService.getAllReview()), OK);
    }

    @GetMapping(path ="/api/review-info")
    public ResponseEntity<Object> getReviewByProductId(@RequestParam @NotNull Long productId){
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), reviewService.getReviewByProductId(productId)), OK);

    }
    @GetMapping(path ="/review-info")
    public ResponseEntity<Object> getReviewById(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), reviewService.getReviewById(id)), OK);
    }

    @PostMapping(path = "/api/add-review")
    public ResponseEntity<Object> addNewReview(@RequestBody @Valid NewReviewRequest review){
        reviewService.addReview(review);
        return new ResponseEntity<>(new ResponseDto(OK.value(), "add review success"), OK);

    }

    @PostMapping(path = "/api/add-reply")
    public ResponseEntity<Object> addReply(@RequestBody @Valid NewReviewRequest reviewDto){
        reviewService.addReply(reviewDto);
        return new ResponseEntity<>(new ResponseDto(OK.value(), "add review success"), OK);

    }
}
