package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.yandex.practicum.filmorate.validator.BirthdayValidator;

@Constraint(validatedBy = BirthdayValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthday {
    String message() default "Дата рождения не может быть в будущем";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}