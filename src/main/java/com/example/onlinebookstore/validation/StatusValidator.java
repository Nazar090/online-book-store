package com.example.onlinebookstore.validation;

import com.example.onlinebookstore.model.Order;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, Order.Status> {
    @Override
    public boolean isValid(Order.Status status, ConstraintValidatorContext context) {
        try {
            Order.Status.valueOf(String.valueOf(status));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
