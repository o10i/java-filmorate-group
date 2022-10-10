package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Film {
    Long id;
    @NotBlank
    final String name;
    @Size(max = 200)
    final String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate releaseDate;
    @Positive
    final int duration;
    final Set<Long> likes = new HashSet<>();

}
