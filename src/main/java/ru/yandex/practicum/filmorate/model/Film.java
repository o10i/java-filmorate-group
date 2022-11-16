package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Builder
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Film {
    Long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;
    @Positive
    int duration;
    int rate;
    Mpa mpa;

    List<Genre> genres;
}
