package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.HashSet;
import java.util.List;

@Component
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    private static final String INSERT_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_POPULAR_QUERY = "SELECT id, name, description, release_date, duration, rating_id " +
            "FROM films LEFT JOIN film_likes ON films.id = film_likes.film_id " +
            "GROUP BY films.id ORDER BY COUNT(film_likes.user_id) DESC LIMIT ?";
    private static final String GET_LIKES_QUERY = "SELECT user_id FROM film_likes WHERE film_id = ?";

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(INSERT_QUERY, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update(DELETE_QUERY, filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        return jdbcTemplate.query(GET_POPULAR_QUERY, (rs, rowNum) -> new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_Date").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(getLikes(rs.getLong("id"))),
                        mpaService.getMpaById(rs.getInt("rating_id")),
                        genreService.getFilmGenres(rs.getLong("id"))),
                count);
    }

    public List<Long> getLikes(Long filmId) {
        return jdbcTemplate.query(GET_LIKES_QUERY, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }
}