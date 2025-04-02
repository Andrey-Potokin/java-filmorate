package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.ValidName;
import ru.yandex.practicum.filmorate.model.User;

public class NameValidator implements ConstraintValidator<ValidName, User> {

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user.getName() == null || user.getName().isBlank()) {
            return user.getLogin() != null && !user.getLogin().isBlank();
        }
        return true;
    }
}
