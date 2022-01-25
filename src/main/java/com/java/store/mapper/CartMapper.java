package com.java.store.mapper;

import com.java.store.dto.CartDto;
import com.java.store.dto.ProductDto;
import com.java.store.dto.UserDto;
import com.java.store.module.Cart;
import com.java.store.module.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartMapper implements BaseMapper<CartDto, Cart>{
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public CartMapper(UserMapper userMapper, ProductMapper productMapper) {
        this.userMapper = userMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Cart DtoToEntity(CartDto cartDto) {
        Cart res = new Cart();
        AtomicLong totalPrice = new AtomicLong();
        res.setUser(userMapper.DtoToEntity(cartDto.getUser()));
        Map<Product, Integer> productSet = new HashMap<>();
        cartDto.getProducts().forEach(productDto -> {
            productSet.put(productMapper.DtoToEntity(productDto), productDto.getAmount());
            totalPrice.addAndGet(productDto.getPrice() * productDto.getAmount());
        });
        res.setProducts(productSet);
        res.setTotalPrice(totalPrice.get());
        return res;
    }

    @Override
    public CartDto EntityToDto(Cart cart) {
        Set<ProductDto> productDtoSet = new HashSet<>();
        cart.getProducts().forEach((product, amount) -> {
            ProductDto productDto =productMapper.EntityToDto(product);
            productDto.setAmount(amount);
            productDtoSet.add(productDto);
        });
        UserDto userDto = userMapper.EntityToDto(cart.getUser());
        return new CartDto(productDtoSet, userDto);
    }
}
