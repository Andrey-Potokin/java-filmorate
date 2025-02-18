package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.ValidBirthday;
import ru.yandex.practicum.filmorate.annotation.ValidLogin;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "email")
public class User {

    private Integer id;
    @Email(message = "Некорректный Email")
    private String email;
    @ValidLogin
    private String login;
    private String name;
    @NotNull(message = "Дата рождения должна быть указана")
    @ValidBirthday
    private LocalDate birthday;
}