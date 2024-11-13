package com.shop.bookshop.util.constant;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class FormatterUtil {
    public String formatCurrency(Double amount) {
        DecimalFormat df = new DecimalFormat("#,###");

        return df.format(amount) + " VNĐ";
    }
}
