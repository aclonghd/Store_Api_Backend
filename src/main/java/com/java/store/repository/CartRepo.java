package com.java.store.repository;

import com.java.store.module.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {

    @Query(value = "select * from carts c where c.id = :cart_id and c.user_id in (select id from users u where u.username = :username)",
        nativeQuery = true)
    Cart getByIdAndUsername(@Param("cart_id") String cartId,@Param("username") String username);

    @Query(value = "select * from carts c where c.id = :cart_id and c.user_id in (select id from users u where u.phone_number = :phone_number)", nativeQuery = true)
    Optional<Cart> getByPhoneNumberAndId(@Param("phone_number") String phoneNumber, @Param("cart_id") String id);
}
