package com.java.store.controller;

import com.java.store.dto.CartDto;
import com.java.store.dto.response.ResponseDto;
import com.java.store.service.CartService;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "carts")
@Validated
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(path = "/api/cart-info")
    public ResponseEntity<Object> getCartByPhoneNumberAndId(@RequestParam @NotBlank String phoneNumber, @RequestParam @NotBlank String id){
        ResponseDto res = new ResponseDto(OK.value(),OK.toString(), cartService.getCartByPhoneNumberAndId(phoneNumber, id));
        return new ResponseEntity<>(res, OK);
    }

    @PostMapping(path = "/api/add-cart")
    public ResponseEntity<Object> addCart(@RequestBody @Valid CartDto cart){
        return new ResponseEntity<>(new ResponseDto(
                OK.value(),
                OK.toString(),
                cartService.addCart(cart)
        ), OK);
    }

    @GetMapping(path = "all-cart-info")
    public ResponseEntity<Object> getAllCart(){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), cartService.getAllCart());
        return new ResponseEntity<>(responseDto, OK);
    }
    @PostMapping(path = "update-cart")
    public ResponseEntity<Object> updateCart(@RequestBody @Valid CartDto cartDto){
        cartService.updateCart(cartDto);
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString()), OK);
    }
}
