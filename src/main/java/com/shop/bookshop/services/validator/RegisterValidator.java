package com.shop.bookshop.services.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.bookshop.domain.Dto.UserRegistrationDto;
import com.shop.bookshop.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, UserRegistrationDto> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(UserRegistrationDto user, ConstraintValidatorContext context) {
        boolean valid = true;

        // check email
        if (userService.checkEmailExist(user.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email đã tồn tại")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
