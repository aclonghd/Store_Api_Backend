package com.java.store.service;


import com.java.store.dto.CartDto;
import com.java.store.mapper.CartMapper;
import com.java.store.module.Cart;
import com.java.store.module.Discount;
import com.java.store.module.Users;
import com.java.store.repository.CartRepo;
import com.java.store.repository.DiscountRepo;
import com.java.store.repository.ProductRepo;
import com.java.store.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    @Autowired
    private final CartRepo cartRepo;
    private final CartMapper cartMapper;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final DiscountRepo discountRepo;

    public CartDto getCartById(String id) throws Exception{
        if(cartRepo.existsById(id)){
            Cart cart = cartRepo.findById(id).get();

            return cartMapper.EntityToDto(cart);
        }
        throw new Exception("Bad Request");
    }

    public CartDto getCartByIdAndUsername(String id, String username) throws Exception{
        if(cartRepo.existsByIdAndUserUsername(id, username)){
            Cart cart = cartRepo.getByIdAndUsername(id, username);
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
        if(cartDto.getDiscountCode() != null){
            List<String> discountCodeList = new ArrayList<>(cartDto.getDiscountCode());
            for (String discountCode : discountCodeList) {
                Discount discount = discountRepo.getByDiscountCode(discountCode);
                if (discount.getExpiryDate().isBefore(LocalDateTime.now())) {
                    throw new Exception("Discount code is expired");
                }
                cart.setTotalPrice(cart.getTotalPrice() - discount.getDiscountValue());
            }
        }

        cart.setUser(user);
        cartRepo.save(cart);
    }

    public void updateCart(Cart cart){

    }

}
