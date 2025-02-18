package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        for (User oldUser : users.values()) {
            if (oldUser.getEmail().equals(user.getEmail())) {
                log.error("Этот Email уже используется");
                throw new DuplicatedDataException("Этот Email уже используется");
            }
        }
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id = {} создан", user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User currentUser = users.get(newUser.getId());
                if (users.values().stream().noneMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
                    if (newUser.getEmail() != null) currentUser.setEmail(newUser.getEmail());
                    if (newUser.getLogin() != null) currentUser.setLogin(newUser.getLogin());
                    if (newUser.getName() != null) currentUser.setName(newUser.getName());
                    if (newUser.getBirthday() != null) currentUser.setBirthday(newUser.getBirthday());
                } else {
                    log.error("Этот Email уже используется");
                    throw new DuplicatedDataException("Этот Email уже используется");
                }
            users.replace(newUser.getId(), currentUser);
            log.info("Пользователь с id = {} обновлен", newUser.getId());
            return currentUser;
        }
        log.error("Пользователь с id = {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}