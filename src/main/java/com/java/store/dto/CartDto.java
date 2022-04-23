package com.java.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CartDto {
    private Set<ProductDto> products;
    private UserDto user;
    private Set<String> discountCode;
    private float totalPrice;
    private String appointmentDate;
    private String paymentMethods;
    private Timestamp timestamp;
    private String status;

    public CartDto(Set<ProductDto> products, UserDto userDto){
        this.user = userDto;
        this.products = products;
        this.totalPrice = 0;
        products.forEach(productDto -> totalPrice += productDto.getPrice()* productDto.getAmount());
    }
}
