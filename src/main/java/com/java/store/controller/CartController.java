package com.java.store.controller;

import com.java.store.dto.CartDto;
import com.java.store.dto.ResponseDto;
import com.java.store.enums.Role;
import com.java.store.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "cart")
@AllArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @GetMapping(path = "/api/cart-info")
    public ResponseEntity<Object> getCartByPhoneNumberAndId(@RequestParam String phoneNumber, String id){
        try{
            ResponseDto res = new ResponseDto(OK.value(),OK.toString(), cartService.getCartByPhoneNumberAndId(phoneNumber, id));
            return new ResponseEntity<>(res, OK);
        } catch (Exception ex){
            ResponseDto res = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(res, BAD_REQUEST);
        }
    }

    @PostMapping(path = "/api/add-cart")
    public ResponseEntity<Object> addCart(@RequestBody CartDto cart){
        try {

            return new ResponseEntity<>(new ResponseDto(
                    OK.value(),
                    OK.toString(),
                    cartService.addCart(cart)
            ), OK);
        } catch (Exception ex){
            ResponseDto res = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(res, BAD_REQUEST);
        }
    }

    @GetMapping(path = "all-cart-info")
    public ResponseEntity<Object> getAllCart(){
        ResponseDto responseDto = new ResponseDto();
        try {
            responseDto.setResult(cartService.getAllCart());
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception exception){
            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(exception.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }
    @PostMapping(path = "update-cart")
    public ResponseEntity<Object> updateCart(@RequestBody CartDto cartDto){
        ResponseDto responseDto = new ResponseDto();
        try {
            cartService.updateCart(cartDto);
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception exception){
            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(exception.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }
}
