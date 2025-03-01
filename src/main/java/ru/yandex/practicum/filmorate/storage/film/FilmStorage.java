package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    void delete(Integer id);

    Film update(Film newFilm);

    List<Film> findAll();

    Film findById(Integer filmId);
}
