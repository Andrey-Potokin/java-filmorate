package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    private  final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @DeleteMapping(value = "/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Integer userId) {
        userStorage.delete(userId);
    }

    @PutMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User newUser) {
        return userStorage.update(newUser);
    }

    @GetMapping(value = "/users")
    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    @GetMapping(value = "/users/{userId}")
    public User findById(@PathVariable Integer userId) {
        return userStorage.findById(userId);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> findFriends(@PathVariable Integer id) {
        return userService.findFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> findFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findCommonFriends(id, otherId);
    }
}