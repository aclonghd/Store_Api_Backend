package com.java.store.repository;

import com.java.store.module.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, String> {
}
