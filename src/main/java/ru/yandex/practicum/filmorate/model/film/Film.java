package ru.yandex.practicum.filmorate.model.film;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Create;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Update;

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
    @NotNull(groups = {Update.class})
    Long id;
    @NotBlank(groups = {Create.class, Update.class}, message = "Название фильма не может быть пустым.")
    String name;
    @NotNull(groups = {Create.class, Update.class})
    @Size(max = 200)
    String description;
    @NotNull(groups = {Create.class, Update.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;
    @Positive(groups = {Create.class, Update.class})
    int duration;
    int rate;
    @NotNull(groups = {Create.class, Update.class})
    Mpa mpa;
    LinkedHashSet<Genre> genres;
    LinkedHashSet<Director> directors;
}
