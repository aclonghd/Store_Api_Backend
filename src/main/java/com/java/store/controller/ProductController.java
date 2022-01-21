package com.java.store.controller;

import com.java.store.module.Product;
import com.java.store.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "product")
@AllArgsConstructor
public class ProductController{
    @Autowired
    private final ProductService productService;

    @GetMapping(path = "api/product-info")
    public ResponseEntity<Product> getProduct(@RequestBody Long productId){
        return new ResponseEntity<>(productService.getProduct(productId), HttpStatus.OK);
    }

    @GetMapping(path = "api")
    public ResponseEntity<List<Product>> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(), OK);
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product){
        productService.addProduct(product);
        return new ResponseEntity<>(OK);
    }


}
