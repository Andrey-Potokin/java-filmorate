package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLikeFilm(Integer filmId, Integer userId) {
        User user = userStorage.findById(userId);
        filmStorage.findById(filmId).addLike(user);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}", userId, filmId);
    }

    public void deleteLikeFilm(Integer filmId, Integer userId) {
        User user = userStorage.findById(userId);
        filmStorage.findById(filmId).deleteLike(user);
        log.info("Пользователь с id = {} удалил лайк фильму с id = {}", userId, filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        log.info("Пользователь получил список популярных фильмов");
        return filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
