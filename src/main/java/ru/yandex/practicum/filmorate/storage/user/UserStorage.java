package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);
    void delete(Integer id);
    User update(User newUser);
    List<User> findAll();
    User findById(Integer id);
}
