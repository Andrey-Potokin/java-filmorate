package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
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

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(id)) {
            users.remove(id);
            log.info("Пользователь с id = {} удален", id);
        } else {
            log.error("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public User update(User newUser) {
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

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(id)) {
            return users.get(id);
        }
        log.error("Пользователь с id = {} не найден", id);
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}