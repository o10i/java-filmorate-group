package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;


import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Follow {
    @NotNull
    final Long userId;
    @NotNull
    final Long friendId;
}
