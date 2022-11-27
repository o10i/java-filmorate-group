package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.review.validation.OnCreate;
import ru.yandex.practicum.filmorate.model.review.validation.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @NotNull(groups = OnUpdate.class)
    Long reviewId;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    String content;
    @JsonProperty("isPositive")
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    Boolean positive;
    @NotNull(groups = OnCreate.class)
    Long filmId;
    @NotNull(groups = OnCreate.class)
    Long userId;
    Long useful;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("content", content);
        values.put("is_positive", positive);
        values.put("film_id", filmId);
        values.put("user_id", userId);
        return values;
    }
}
