package com.java.store.mapper;

import com.java.store.dto.CartDto;
import com.java.store.module.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartMapper implements BaseMapper<CartDto, Cart>{

    @Override
    public Cart DtoToEntity(CartDto cartDto) {
        Cart res = new Cart();
        res.setAppointmentDate(cartDto.getAppointmentDate());
        res.setPaymentMethods(cartDto.getPaymentMethods());
        res.setTimestamp(cartDto.getTimestamp());
        res.setStatus(cartDto.getStatus());
        return res;
    }

    @Override
    public CartDto EntityToDto(Cart cart) {
        CartDto res =new CartDto();
        res.setStatus(cart.getStatus());
        res.setTimestamp(cart.getTimestamp());
        res.setAppointmentDate(cart.getAppointmentDate());
        res.setPaymentMethods(cart.getPaymentMethods());
        return res;
    }
}
