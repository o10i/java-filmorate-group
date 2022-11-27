package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;

import java.util.Collection;

public interface ReviewStorage {
    Review create(Review review);
    Review update(Review review);
    Review getById(Long reviewId);
    void delete(Long reviewId);
    Collection<Review> getAll();
    Collection<Review> getAll(Long filmId, Integer count);
}
