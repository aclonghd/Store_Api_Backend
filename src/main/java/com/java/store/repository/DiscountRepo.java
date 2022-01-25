package com.java.store.repository;

import com.java.store.module.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepo extends JpaRepository<Discount, Long> {
    Optional<Discount> findByDiscountCode(String discountCode);

    default Discount getByDiscountCode(String discountCode) throws Exception{
        if(findByDiscountCode(discountCode).isPresent())
            return findByDiscountCode(discountCode).get();
        throw new Exception(String.format("Discount code: %s is incorrect", discountCode));
    }
}
