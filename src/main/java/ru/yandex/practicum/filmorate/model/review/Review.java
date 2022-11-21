package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @JsonProperty("reviewId")
    Long id;
    @NotBlank
    String content;
    @JsonProperty("isPositive")
    @NotNull
    Boolean positive;
    @NotNull
    Long filmId;
    @NotNull
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
