package com.java.store.controller;

import com.java.store.dto.CartDto;
import com.java.store.dto.ResponseDto;
import com.java.store.module.Cart;
import com.java.store.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "cart")
@AllArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @GetMapping(path = "cart-info")
    public ResponseEntity<Object> getCartById(@RequestParam String id){
        try{
            ResponseDto res = new ResponseDto(OK.value(),OK.toString(),cartService.getCartById(id));
            return new ResponseEntity<>(res, OK);

        } catch (Exception ex){
            ResponseDto res = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(res, BAD_REQUEST);
        }
    }

    @PostMapping(path = "add-cart")
    public ResponseEntity<Object> addCart(@RequestBody CartDto cart){
        try {
            cartService.addCart(cart);
            return new ResponseEntity<>(new ResponseDto(
                    OK.value(),
                    OK.toString()
            ), OK);
        } catch (Exception ex){
            ResponseDto res = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(res, BAD_REQUEST);
        }
    }

    @GetMapping(path = "all-cart-info")
    public ResponseEntity<Object> getAllCart(){
        return new ResponseEntity<>(cartService.getAllCart(), OK);
    }
}