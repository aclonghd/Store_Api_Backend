package com.java.store.dto;

import com.java.store.dto.response.UserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CartDto {
    private String id;
    @NotNull
    private Set<ProductDto> products;
    @NotNull
    private UserResponse user;
    private Set<String> discountCode;
    private float totalPrice;
    @NotNull
    private String appointmentDate;
    @NotNull
    private String paymentMethods;
    @NotNull
    private Timestamp timestamp;
    private String status;

    public CartDto(Set<ProductDto> products, UserResponse userResponse){
        this.user = userResponse;
        this.products = products;
        this.totalPrice = 0;
        products.forEach(productDto -> totalPrice += productDto.getPrice()* productDto.getAmount());
    }
}
