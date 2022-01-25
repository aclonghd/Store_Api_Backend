package com.java.store.repository;

import com.java.store.module.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepo extends JpaRepository<Cart, String> {

    @Query(value = "select * from carts c where c.id = :cartId and c.user_id in (select id from users u where u.username = :username)",
        nativeQuery = true)
    Cart getByIdAndUsername(@Param("cartId") String cartId,@Param("username") String username);

    boolean existsByIdAndUserUsername(String id, String username);
}
