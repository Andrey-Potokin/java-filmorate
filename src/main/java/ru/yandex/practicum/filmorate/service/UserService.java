package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(FriendStorage friendStorage) {
        this.friendStorage = friendStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья!");
        }
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей!");
        }
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> findFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        if (userId != null) {
            friends = friendStorage.getFriends(userId);
        }
        return friends;
    }

    public List<User> findCommonFriends(Long firstUserId, Long secondUserId) {
        if (firstUserId == null || secondUserId == null) {
            throw new ValidationException("Оба идентификатора пользователей должны быть заданы!");
        }
        return friendStorage.findCommonFriends(firstUserId, secondUserId);
    }
}