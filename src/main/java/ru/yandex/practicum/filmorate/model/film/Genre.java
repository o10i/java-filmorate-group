package ru.yandex.practicum.filmorate.model.film;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Builder
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Genre {

    Long id;

    String name;
}
