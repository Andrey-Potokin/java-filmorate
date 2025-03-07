package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        log.info("Пользователь с id = {} добавил в друзья пользователя с id = {}", id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(id);
        log.info("Пользователь с id = {} удалил из друзей пользователя с id = {}", id, friendId);
    }

    public List<User> findFriends(Integer id) {
        log.info("Пользователь с id = {} получил список друзей", id);
        return userStorage.findById(id).getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) {
        List<User> friends = findFriends(id);
        List<User> otherFriends = findFriends(otherId);
        log.info("Пользователь с id = {} получил список общих друзей", id);
        return friends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }
}