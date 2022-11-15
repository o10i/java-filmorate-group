package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Builder
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Genre {
    Long id;

    final String name;
}
