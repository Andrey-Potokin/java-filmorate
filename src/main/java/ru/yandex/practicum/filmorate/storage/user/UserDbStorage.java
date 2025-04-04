package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ? ";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.info("Добавлен новый пользователь с ID={}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (findById(user.getId()) != null) {
            jdbcTemplate.update(UPDATE_QUERY,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Пользователь с ID={} успешно обновлен", user.getId());
            return user;
        } else {
            throw new NotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
    }

    @Override
    public void delete(Long userId) {
        if (userId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (jdbcTemplate.update(DELETE_QUERY, userId) == 0) {
            throw new NotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
    }

    @Override
    public List<User> findAll() {
        log.info("Получен список всех пользователей");
        return jdbcTemplate.query(FIND_ALL_QUERY, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                null)
        );
    }

    @Override
    public User findById(Long userId) {
        if (userId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        User user;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(FIND_BY_ID_QUERY, userId);
        if (userRows.first()) {
            user = new User(
                    userRows.getLong("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(),
                    null);
        } else {
            throw new NotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        return user;
    }
}