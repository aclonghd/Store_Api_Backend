package com.java.store.controller;

import com.java.store.dto.ProductDto;
import com.java.store.dto.response.ResponseDto;
import com.java.store.service.ProductService;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "products")
@Validated
public class ProductController{
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "api/product-info")
    public ResponseEntity<Object> getProduct(@RequestParam @NotNull Long id){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(),  productService.getProduct(id));
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping(path = "api/product-info-url")
    public ResponseEntity<Object> getProductByTitleUrl(@RequestParam @NotBlank String titleUrl) {
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), productService.getProductByTitleUrl(titleUrl));
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping(path = "api/all-product-info")
    public ResponseEntity<Object> getAllProduct(){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), productService.getAllProduct());
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping(path = "get-all-productTag")
    public ResponseEntity<Object> getAllProductTag(){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), productService.getAllProductTag());
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping(path = "api/product")
    public ResponseEntity<Object> findAllByProductTag(@RequestParam @NotBlank String productTag){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), productService.findAllByProductTag(productTag));
        return new ResponseEntity<>(responseDto, OK);
    }

    @PostMapping(path = "add-product", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> addProduct(@RequestBody @Valid ProductDto product, @ModelAttribute Set<MultipartFile> files) {
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), productService.addProduct(product, files)), OK);
    }

    @PutMapping(path = "update-product")
    public ResponseEntity<Object> updateProduct(@RequestBody @Valid ProductDto product) {
        productService.updateProduct(product);
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString()), OK);
    }

    @DeleteMapping(path = "delete-product")
    public ResponseEntity<Object> deleteProduct(@RequestParam @NotNull Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString()), OK);

    }
    @PutMapping(path = "{product-id}/import-image")
    public ResponseEntity<Object> importImage(@PathVariable("product-id") Long productId,@ModelAttribute List<MultipartFile> fileUploadList){
        return new ResponseEntity<>(new ResponseDto(OK.value(), "upload image success", productService.uploadPhotosToProduct(fileUploadList, productId)), OK);

    }
    @DeleteMapping(path = "{product-id}/delete-image")
    public ResponseEntity<Object> deletePhotoFromProduct(@PathVariable("product-id") @NotNull Long productId, @RequestParam @NotBlank String photoId){
        productService.removePhotoFromProduct(photoId, productId);
        return new ResponseEntity<>(new ResponseDto(OK.value(), "delete success"), OK);

    }
}
