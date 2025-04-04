package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    private static final String INSERT_QUERY = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE friends SET user_id = ? AND friend_id = ? AND status = ? " +
            "WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String GET_ALL_QUERY = "SELECT friend_id, email, login, name, birthday FROM friends" +
            " INNER JOIN users ON friends.friend_id = users.id WHERE friends.user_id = ?";
    private static final String GET_FRIENDS_QUERY = "SELECT friend_id FROM friends WHERE user_id = ?";

    @Autowired
    public FriendStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        if ((user != null) && (friend != null)) {
            boolean status = false;
            if (friend.getFriends().contains(userId)) {
                status = true;
                jdbcTemplate.update(UPDATE_QUERY, friendId, userId, true, friendId, userId);
            }
            jdbcTemplate.update(INSERT_QUERY, userId, friendId, status);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        if ((user != null) && (friend != null)) {
            jdbcTemplate.update(DELETE_QUERY, userId, friendId);
            if (friend.getFriends().contains(userId)) {
                jdbcTemplate.update(UPDATE_QUERY, friendId, userId, false, friendId, userId);
            }
        }
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.findById(userId);
        if (user != null) {
            return jdbcTemplate.query(GET_ALL_QUERY, (rs, rowNum) -> new User(
                            rs.getLong("friend_id"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate(),
                            null),
                    userId
            );
        } else {
            return null;
        }
    }

    public List<User> findCommonFriends(Long firstUserId, Long secondUserId) {
        List<Long> friendsUser1 = jdbcTemplate.query(GET_FRIENDS_QUERY,
                new Object[]{firstUserId},
                (rs, rowNum) -> rs.getLong("friend_id")
        );

        List<Long> friendsUser2 = jdbcTemplate.query(GET_FRIENDS_QUERY,
                new Object[]{secondUserId},
                (rs, rowNum) -> rs.getLong("friend_id")
        );

        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : friendsUser1) {
            if (friendsUser2.contains(friendId)) {
                User friend = userStorage.findById(friendId);
                if (friend != null) {
                    commonFriends.add(friend);
                }
            }
        }
        return commonFriends;
    }
}