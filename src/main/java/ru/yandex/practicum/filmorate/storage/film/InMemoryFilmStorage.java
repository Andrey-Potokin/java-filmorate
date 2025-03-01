package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм с id = {} создан", film.getId());
        return film;
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(id)) {
            films.remove(id);
            log.info("Фильм с id = {} удален", id);
        } else {
            log.error("Фильм с id = {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            Film currentFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null) currentFilm.setName(newFilm.getName());
            if (newFilm.getDescription() != null) currentFilm.setDescription(newFilm.getDescription());
            if (newFilm.getReleaseDate() != null) currentFilm.setReleaseDate(newFilm.getReleaseDate());
            if (newFilm.getDuration() != null) currentFilm.setDuration(newFilm.getDuration());
            films.replace(newFilm.getId(), currentFilm);
            log.info("Фильм с id = {} обновлен", newFilm.getId());
            return currentFilm;
        }
        log.error("Фильм с id = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Integer id) {
        if (id == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(id)) {
            return films.get(id);
        }
        log.error("Фильм с id = {} не найден", id);
        throw new NotFoundException("Фильм с id = " + id + " не найден");
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
