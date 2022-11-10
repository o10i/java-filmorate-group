package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Follow {
    @NotNull
    final Long friendIdOne;
    @NotNull
    final Long friendIdTwo;
}
