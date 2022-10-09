package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final Set<Long> likes = new HashSet<>();

}
