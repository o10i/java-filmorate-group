package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @JsonProperty("reviewId")
    Long id;
    String content;
    @JsonProperty("isPositive")
    Boolean positive;
    Long filmId;
    Long userId;
    long useful;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("content", content);
        values.put("is_positive", positive);
        values.put("film_id", filmId);
        values.put("user_id", userId);
        values.put("useful", useful);
        return values;
    }
}
