package com.java.store.service;


import com.java.store.dto.CartDto;
import com.java.store.dto.ProductDto;
import com.java.store.dto.UserDto;
import com.java.store.mapper.CartMapper;
import com.java.store.mapper.UserMapper;
import com.java.store.module.Cart;
import com.java.store.module.Discount;
import com.java.store.module.Product;
import com.java.store.module.Users;
import com.java.store.repository.CartRepo;
import com.java.store.repository.DiscountRepo;
import com.java.store.repository.ProductRepo;
import com.java.store.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public CartDto getCartById(String id) throws Exception{
        if(cartRepo.existsById(id)){
            Cart cart = cartRepo.findById(id).get();

            return cartMapper.EntityToDto(cart);
        }
        throw new Exception("Bad Request");
    }

    public CartDto getCartByPhoneNumberAndId(String phoneNumber, String id) {
        return cartMapper.EntityToDto(cartRepo.getByPhoneNumberAndId(phoneNumber, id));
    }

    public List<CartDto> getAllCart(){
        List<CartDto> cartDtoList = new ArrayList<>();
        cartRepo.findAll().forEach(cart -> cartDtoList.add(cartMapper.EntityToDto(cart)));
        return cartDtoList;
    }

    public String addCart(CartDto cartDto) throws Exception{
        float totalPrice = 0;
        Map<Product, Integer> products = new HashMap<>();
        for(ProductDto p : cartDto.getProducts()){
            if(productRepo.existsById(p.getId())){
                Product product = productRepo.getById(p.getId());
                products.put(product, p.getAmount());
                totalPrice += product.getPrice()*p.getAmount();

            } else throw new Exception("Bad request");
        }
        String username = cartDto.getUser().getUsername();
        Users user;
        Cart cart = cartMapper.DtoToEntity(cartDto);
        if(cartDto.getDiscountCode() != null){
            Set<Discount> discountApply = new HashSet<>();
            List<String> discountCodeList = new ArrayList<>(cartDto.getDiscountCode());
            for (String discountCode : discountCodeList) {
                Discount discount = discountRepo.getByDiscountCode(discountCode);
                if (discount.getExpiryDate().isBefore(LocalDateTime.now())) {
                    throw new Exception("Discount code is expired");
                }
                cart.setTotalPrice(totalPrice - discount.getDiscountValue());
                discountApply.add(discount);
            }
            cart.setDiscountApply(discountApply);
        } else cart.setTotalPrice(totalPrice);
        if(username == null || userRepo.findByUsername(username) == null){
            UserDto userDto = cartDto.getUser();
            user = userMapper.DtoToEntity(userDto);
            user.setUsername(passwordEncoder.encode("username"));
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("USER");
            userRepo.save(user);
            userRepo.flush();
        } else user = userRepo.findByUsername(username);
        cart.setUser(user);
        cart.setProducts(products);
        cart.setStatus("Đang chờ xử lý");
        cartRepo.save(cart);
        cartRepo.flush();
        return cart.getId();
    }

    public void updateCart(CartDto cartDto) throws Exception{
        if(cartRepo.existsById(cartDto.getId())){
            Cart cart = cartRepo.findById(cartDto.getId()).get();
            cart.setStatus(cartDto.getStatus());
            cartRepo.save(cart);
            return;
        }
        throw new Exception("Bad Request");
    }

}
