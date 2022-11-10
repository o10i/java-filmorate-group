package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Genre {
    Long id;
    @NotBlank
    final String name;
}
