package com.shop.bookshop.services.validator;

import com.shop.bookshop.domain.Dto.UserRegistrationDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterValidator implements ConstraintValidator<RegisterChecked, UserRegistrationDto> {

    @Override
    public boolean isValid(UserRegistrationDto user, ConstraintValidatorContext context) {
        boolean valid = true;

        // Check if password fields match
        // if (!user.getPassword().equals(user.getConfirmPassword())) {
        //     context.buildConstraintViolationWithTemplate("Passwords must match")
        //             .addPropertyNode("confirmPassword")
        //             .addConstraintViolation()
        //             .disableDefaultConstraintViolation();
        //     valid = false;
        // }

        // Additional validations can be added here

        return valid;
    }
}
