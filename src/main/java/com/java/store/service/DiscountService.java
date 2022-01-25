package com.java.store.service;

import com.java.store.module.Discount;
import com.java.store.repository.DiscountRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DiscountService {
    @Autowired
    private final DiscountRepo discountRepo;

    public void addDiscount(Discount discount){
        discountRepo.save(discount);
    }

    public void deleteDiscount(Discount discount){
        discountRepo.delete(discount);
    }

    public Discount getDiscountInfo(String discountCode) throws  Exception{
        return discountRepo.getByDiscountCode(discountCode);
    }
}
