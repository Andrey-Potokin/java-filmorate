package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.ValidBirthday;
import ru.yandex.practicum.filmorate.annotation.ValidLogin;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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

    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.login = user.login;
        this.name = user.name;
        this.birthday = user.birthday;
    }
}

