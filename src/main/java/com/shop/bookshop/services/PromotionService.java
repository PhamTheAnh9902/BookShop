package com.shop.bookshop.services;

import com.shop.bookshop.domain.Author;
import com.shop.bookshop.domain.Promotion;
import com.shop.bookshop.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Promotion> getAllPromotionPaging(int pageNum) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return promotionRepository.findAll(pageable);
    }

    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public Promotion getPromotionById(long id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        if (promotionOptional.isPresent()) {
            return promotionOptional.get();
        }
        return null;
    }

    public Promotion updatePromotion(Long id, Promotion promotion) {
        Promotion currentPromotion = this.getPromotionById(id);
        if (currentPromotion != null) {
            currentPromotion.setCode(promotion.getCode());
            currentPromotion.setPromotionName(promotion.getPromotionName());
            currentPromotion.setDiscountRate(promotion.getDiscountRate());
            currentPromotion.setStartDate(promotion.getStartDate());
            currentPromotion.setEndDate(promotion.getEndDate());
        }
        return promotionRepository.save(currentPromotion);
    }

    public void deletePromotionById(long id) {
        promotionRepository.deleteById(id);
    }
}
