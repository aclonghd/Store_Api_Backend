package com.java.store.service;


import com.java.store.dto.CartDto;
import com.java.store.mapper.CartMapper;
import com.java.store.module.Cart;
import com.java.store.module.Users;
import com.java.store.repository.CartRepo;
import com.java.store.repository.ProductRepo;
import com.java.store.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    @Autowired
    private final CartRepo cartRepo;
    private final CartMapper cartMapper;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public CartDto getCartById(String id) throws Exception{
        if(cartRepo.existsById(id)){
            Cart cart = cartRepo.findById(id).get();

            return cartMapper.EntityToDto(cart);
        }
        throw new Exception("Bad Request");
    }
    public List<CartDto> getAllCart(){
        List<CartDto> cartDtoList = new ArrayList<>();
        cartRepo.findAll().forEach(cart -> cartDtoList.add(cartMapper.EntityToDto(cart)));
        return cartDtoList;
    }

    public void addCart(CartDto cartDto) throws Exception{
        Cart cart = cartMapper.DtoToEntity(cartDto);
        String username = cart.getUser().getUsername();
        Users user = userRepo.findByUsername(username);
        if(userRepo.findByUsername(username) == null) throw new Exception("User id is invalid");
        cart.setUser(user);
        cartRepo.save(cart);
    }

    public void updateCart(Cart cart){

    }

}
