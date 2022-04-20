package com.java.store.controller;

import com.java.store.dto.ProductDto;
import com.java.store.dto.ResponseDto;
import com.java.store.module.Product;
import com.java.store.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin("*")
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
            List<ProductDto> res = new ArrayList<>();
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

    @GetMapping(path = "api/product-info-url")
    public ResponseEntity<Object> getProductByTitleUrl(@RequestParam String titleUrl){
        ResponseDto responseDto = new ResponseDto();
        try
        {
            List<ProductDto> res = new ArrayList<>();
            res.add(productService.getProductByTitleUrl(titleUrl));
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

    @PostMapping(path = "add-product", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> addProduct(@ModelAttribute ProductDto product){
        ResponseDto responseDto = new ResponseDto();
        try
        {
            return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString(), productService.addProduct(product)), OK);
        } catch (Exception ex){
            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(ex.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @PostMapping(path = "update-product")
    public ResponseEntity<Object> updateProduct(@RequestBody ProductDto product){
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
    public ResponseEntity<Object> deleteProduct(@RequestParam Long id){
        ResponseDto responseDto = new ResponseDto();
        try {
            productService.deleteProduct(id);
            responseDto.setCode(OK.value());
            responseDto.setMessage(OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception exception){
            responseDto.setCode(BAD_REQUEST.value());
            responseDto.setMessage(exception.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }
    @PostMapping(path = "{product-id}/import-image")
    public ResponseEntity<Object> importImage(@PathVariable("product-id") Long productId,@ModelAttribute List<MultipartFile> fileUploadList){
        try{

            return new ResponseEntity<>(new ResponseDto(OK.value(), "upload image success", productService.uploadPhotosToProduct(fileUploadList, productId)), OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(BAD_REQUEST.value(), ex.getMessage()), BAD_REQUEST);
        }
    }
    @DeleteMapping(path = "{product-id}/delete-image")
    public ResponseEntity<Object> deletePhotoFromProduct(@PathVariable("product-id") Long productId, @RequestParam String photoId){
        try{
            productService.removePhotoFromProduct(photoId, productId);
            return new ResponseEntity<>(new ResponseDto(OK.value(), "delete success"), OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(BAD_REQUEST.value(), ex.getMessage()), BAD_REQUEST);
        }
    }
}
