package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Likes {
    @NotNull
    final Long filmId;
    @NotNull
    final Long userId;
}
