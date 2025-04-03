package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM genres";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String GET_FILM_GENRES_QUERY = "SELECT genre_id, name FROM film_genres" +
            " INNER JOIN genres ON genre_id = id WHERE film_id = ?";

    @Autowired
    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(INSERT_QUERY, film.getId(), genre.getId());
            }
        }
    }

    public void delete(Film film) {
        jdbcTemplate.update(DELETE_QUERY, film.getId());
    }

    public List<Genre> getGenres() {
        return jdbcTemplate.query(GET_ALL_QUERY, (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name"))
        );
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Genre genre;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GET_BY_ID_QUERY, genreId);
        if (genreRows.first()) {
            genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );
        } else {
            throw new NotFoundException("Жанр с ID=" + genreId + " не найден!");
        }
        return genre;
    }

    public List<Genre> getFilmGenres(Long filmId) {
        return jdbcTemplate.query(GET_FILM_GENRES_QUERY, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"), rs.getString("name")), filmId
        );
    }
}