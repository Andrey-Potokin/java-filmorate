package ru.yandex.practicum.filmorate.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.ValidBirthday;
import ru.yandex.practicum.filmorate.annotation.ValidLogin;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private FriendshipStatus friendshipStatus;
    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer friendId) {
        friends.remove(friendId);
    }
}