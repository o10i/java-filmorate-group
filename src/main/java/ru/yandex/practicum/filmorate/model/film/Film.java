package ru.yandex.practicum.filmorate.model.film;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.validation.film.FilmReleaseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @NotNull(groups = {Marker.OnUpdate.class})
    Long id;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "Название фильма не может быть пустым.")
    String name;
    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Size(max = 200)
    String description;
    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @FilmReleaseDateConstraint
    LocalDate releaseDate;
    @Positive(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    int duration;
    int rate;
    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    Mpa mpa;
    LinkedHashSet<Genre> genres;
    LinkedHashSet<Director> directors;
}
