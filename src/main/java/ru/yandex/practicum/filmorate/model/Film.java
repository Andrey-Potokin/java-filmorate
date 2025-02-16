package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValid;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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

    public Film(Film film) {
        this.id = film.id;
        this.name = film.name;
        this.description = film.description;
        this.releaseDate = film.releaseDate;
        this.duration = film.duration;
    }
}
