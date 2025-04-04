package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL_QUERY = "SELECT * FROM ratings_mpa";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM ratings_mpa WHERE id = ?";

    @Autowired
    public MpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(GET_ALL_QUERY, (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name"))
        );
    }

    public Mpa getMpaById(Integer mpaId) {
        if (mpaId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Mpa mpa;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(GET_BY_ID_QUERY, mpaId);
        if (mpaRows.first()) {
            mpa = new Mpa(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name")
            );
        } else {
            throw new NotFoundException("Рейтинг с ID=" + mpaId + " не найден!");
        }
        return mpa;
    }
}