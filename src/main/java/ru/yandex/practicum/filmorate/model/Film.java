package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    public static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);
}
