package com.java.store.repository;

import com.java.store.exception.ServiceException;
import com.java.store.module.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface DiscountRepo extends JpaRepository<Discount, Long> {
    Optional<Discount> findByDiscountCode(String discountCode);

    default Discount getByDiscountCode(String discountCode) throws ServiceException{
        if(findByDiscountCode(discountCode).isPresent())
            return findByDiscountCode(discountCode).get();
        throw new ServiceException(HttpStatus.BAD_REQUEST.value(), String.format("Discount code: %s is incorrect", discountCode));
    }
}
