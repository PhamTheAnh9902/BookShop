package com.shop.bookshop.domain.Dto;

import java.io.Serializable;

public class PromotionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private double discountRate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}
