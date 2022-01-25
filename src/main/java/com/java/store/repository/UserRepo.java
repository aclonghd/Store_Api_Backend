package com.java.store.repository;

import com.java.store.module.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import com.java.store.module.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    @Query(value = "select discount_code from discount d where d.id in (select discount_id from users_discounts ud where ud.user_id in (select id from users u where u.username = :username));", nativeQuery = true)
    List<String> findDiscountByUsername(@Param("username") String username);


}
