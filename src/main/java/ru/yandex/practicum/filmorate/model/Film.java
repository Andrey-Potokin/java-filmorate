package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValid;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    @ReleaseDateValid
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Set<Long> likes;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres;

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration,
                Set<Long> likes, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "name", name,
                "description", description,
                "release_Date", releaseDate,
                "duration", duration,
                "rating_id", mpa.getId()
        );
    }
}