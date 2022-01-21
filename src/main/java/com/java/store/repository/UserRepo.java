package com.java.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.java.store.module.Users;

public interface UserRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
