package com.java.store.controller;

import com.java.store.dto.ResponseDto;
import com.java.store.module.Product;
import com.java.store.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "product")
@AllArgsConstructor
public class ProductController{
    @Autowired
    private final ProductService productService;

    @GetMapping(path = "api/product-info")
    public ResponseEntity<Object> getProduct(@RequestParam Long id){
        ResponseDto responseDto = new ResponseDto();
        try
        {
            List<Product> res = new ArrayList<>();
            res.add(productService.getProduct(id));
            responseDto.setResult(res);
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex){

            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @GetMapping(path = "api/all-product-info")
    public ResponseEntity<Object> getAllProduct(){
        ResponseDto responseDto = new ResponseDto();
        try
        {
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            responseDto.setResult(productService.getAllProduct());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex){

            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @PostMapping(path = "add-product")
    public ResponseEntity<Object> addProduct(@RequestBody Product product){
        ResponseDto responseDto = new ResponseDto();
        try
        {
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            productService.addProduct(product);
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex){
            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @PostMapping(path = "update-product")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product){
        ResponseDto responseDto = new ResponseDto();
        try {
            productService.updateProduct(product);
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception exception){
            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(exception.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "delete-product")
    public ResponseEntity<Object> deleteProduct(@RequestBody Collection<Long> listProductId){
        ResponseDto responseDto = new ResponseDto();
        try {
            productService.deleteProduct(listProductId);
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
