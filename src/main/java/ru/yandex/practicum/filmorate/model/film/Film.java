package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValid;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
public class Film {

    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;
    @NotNull(message = "Дата выхода фильма не может быть пустой")
    @ReleaseDateValid
    private LocalDate releaseDate;
    @NotNull(message = "Длительность фильма не может быть пустой")
    @Positive(message = "Длительность фильма должна быть больше 0")
    private Integer duration;
    private MpaRating mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<User> likes = new HashSet<>();

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void deleteGenre(Genre genre) {
        genres.remove(genre);
    }

    public void addLike(User user) {
        likes.add(user);
    }

    public void deleteLike(User user) {
        likes.remove(user);
    }
}