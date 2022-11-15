package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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

    Set<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("releaseDate", releaseDate);
        values.put("duration", duration);
        values.put("rate", rate);
        values.put("mpa", mpa);
        return values;
    }
}
