package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.ValidLogin;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    @Override
    public void initialize(ValidLogin constraintAnnotation) {
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null || login.isEmpty()) {
            return false;
        }
        return login.matches("^[a-zA-Z0-9]+$");
    }
}