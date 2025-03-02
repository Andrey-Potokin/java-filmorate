package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@RestController
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @DeleteMapping(value = "/films/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(@PathVariable Integer filmId) {
        filmStorage.delete(filmId);
    }

    @PutMapping(value = "/films")
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.update(film);
    }

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping(value = "/films/{filmId}")
    public Film findById(@PathVariable Integer filmId) {
        return filmStorage.findById(filmId);
    }

    @PutMapping(value = "/films/{filmId}/like/{userId}")
    public void addLikeFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmService.addLikeFilm(filmId, userId);
    }

    @DeleteMapping(value = "/films/{filmId}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmService.deleteLikeFilm(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findPopularFilms(count);
    }
}