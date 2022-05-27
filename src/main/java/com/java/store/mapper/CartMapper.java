package com.java.store.mapper;

import com.java.store.dto.CartDto;
import com.java.store.dto.ProductDto;
import com.java.store.module.Cart;
import com.java.store.module.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class CartMapper implements BaseMapper<CartDto, Cart>{
    private final UserMapper userMapper;
    private  final ProductMapper productMapper;
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
        res.setId(cart.getId());
        res.setUser(userMapper.EntityToDto(cart.getUser()));
        Set<ProductDto> products = new HashSet<>();
        for(Map.Entry<Product, Integer> entry: cart.getProducts().entrySet()){
            ProductDto product = productMapper.EntityToDto(entry.getKey());
            product.setAmount(entry.getValue());
            products.add(product);
        }
        res.setProducts(products);
        res.setTotalPrice(cart.getTotalPrice());
        return res;
    }
}
