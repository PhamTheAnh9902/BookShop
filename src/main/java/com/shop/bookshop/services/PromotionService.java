package com.shop.bookshop.services;

import com.shop.bookshop.domain.Promotion;
import com.shop.bookshop.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    public Promotion applyPromotion(String promoCode){
        Optional<Promotion> promotionOptional = promotionRepository.findByCode(promoCode);
        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            if (LocalDate.now().isBefore(promotion.getEndDate()) &&
                    LocalDate.now().isAfter(promotion.getStartDate())){
                return promotion;
            }
        }
        return null;
    }
}
