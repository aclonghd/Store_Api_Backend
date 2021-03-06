package com.java.store.controller;

import com.java.store.dto.response.ResponseDto;
import com.java.store.module.Discount;
import com.java.store.service.DiscountService;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "discounts")
public class DiscountController {
    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping(path = "add-discount")
    public ResponseEntity<Object> addDiscount(@RequestBody Discount discount){
        discountService.addDiscount(discount);
        return new ResponseEntity<>(new ResponseDto(OK.value(), "Add discount success"), OK);
    }

    @DeleteMapping(path = "delete-discount")
    public ResponseEntity<Object> deleteDiscount(@RequestBody Discount discount){
        discountService.deleteDiscount(discount);
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString()), OK);
    }

    @GetMapping(path = "discount-info")
    public ResponseEntity<Object> getDiscountInfo(@RequestParam String discountCode){
        try {
            Discount discount = discountService.getDiscountInfo(discountCode);
            return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), discount), OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(BAD_REQUEST.value(), ex.getMessage()), BAD_REQUEST);
        }
    }
}
