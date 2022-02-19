package com.java.store.controller;

import com.java.store.dto.NewReviewDto;
import com.java.store.dto.ResponseDto;
import com.java.store.module.Review;
import com.java.store.service.ReviewService;
import lombok.AllArgsConstructor;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "review")
@CrossOrigin("*")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping(path = "all-review")
    public ResponseEntity<Object> getAllReview(){
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), reviewService.getAllReview()), OK);
    }

    @PostMapping(path = "add-review")
    public ResponseEntity<Object> addNewReview(@RequestBody NewReviewDto review){
        try {
            reviewService.addReview(review);
            return new ResponseEntity<>(new ResponseDto(OK.value(), "add review success"), OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseDto(BAD_REQUEST.value(), exception.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping(path = "add-reply")
    public ResponseEntity<Object> addReply(@RequestBody NewReviewDto reviewDto){
        try {
            reviewService.addReply(reviewDto);
            return new ResponseEntity<>(new ResponseDto(OK.value(), "add review success"), OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseDto(BAD_REQUEST.value(), exception.getMessage()), BAD_REQUEST);
        }
    }
}
