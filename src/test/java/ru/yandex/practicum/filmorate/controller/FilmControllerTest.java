package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private Validator validator;
    private FilmController fc;
    private Film film;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        fc = new FilmController();
        film = new Film();
        film.setId(0);
        film.setName("Фильм");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2025, Month.JANUARY, 1));
        film.setDuration(100);
    }

    @Test
    public void testCreateFilmWithNameBlank() {
        film.setName("  ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Название фильма не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithNameNull() {
        film.setName(null);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Название фильма не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithDescriptionNull() {
        film.setDescription(null);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Описание фильма не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithDescriptionLength200() {
        film.setDescription("Это корректное описание.");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }

    @Test
    public void testCreateFilmWithDescriptionLength201() {
        film.setDescription("A".repeat(201));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Должны быть нарушения валидации");
        assertEquals(1, violations.size(), "Должно быть одно нарушение валидации");
        assertEquals("Описание не должно превышать 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    public void testReleaseDateTooEarly() {
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Должны быть нарушения валидации");
        assertEquals(1, violations.size(), "Должно быть одно нарушение валидации");
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    public void testReleaseDateValid() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть нарушений валидации");
    }

    @Test
    public void testCreateFilmWithDurationNegative() {
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Длительность фильма должна быть больше 0", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithDurationZero() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertEquals("Длительность фильма должна быть больше 0", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithDurationPositive() {
        film.setDuration(1);

        fc.create(film);

        List<Film> films = fc.getAll();
        assertEquals(1, films.size());
        assertEquals(1, films.getFirst().getDuration());
    }

    @Test
    public void testUpdateFilmNewName() throws IllegalAccessException {
        fc.create(film);
        film.setName("Новый фильм");

        fc.update(film);

        List<Film> films = fc.getAll();
        assertEquals(1, films.size());
        assertEquals("Новый фильм", films.getFirst().getName());
    }

    @Test
    public void testUpdateFilmNewDescription() throws IllegalAccessException {
        fc.create(film);
        film.setDescription("Новое описание фильма");

        fc.update(film);

        List<Film> films = fc.getAll();
        assertEquals(1, films.size());
        assertEquals("Новое описание фильма", films.getFirst().getDescription());
    }

    @Test
    public void testUpdateFilmNewReleaseDate() throws IllegalAccessException {
        fc.create(film);
        LocalDate newDate = LocalDate.of(2025, Month.JANUARY, 1);
        film.setReleaseDate(newDate);

        fc.update(film);

        List<Film> films = fc.getAll();
        assertEquals(1, films.size());
        assertEquals(newDate, films.getFirst().getReleaseDate());
    }

    @Test
    public void testUpdateFilmNewDuration() throws IllegalAccessException {
        fc.create(film);
        film.setDuration(1000);

        fc.update(film);

        List<Film> films = fc.getAll();
        assertEquals(1, films.size());
        assertEquals(1000, films.getFirst().getDuration());
    }
}


