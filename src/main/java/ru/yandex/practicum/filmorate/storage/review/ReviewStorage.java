package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

public interface ReviewStorage {
    void create(Review review);
    void update(Review review);
    Review get(Long reviewId);
    void delete(Long reviewId);
    Collection<Review> getAll();
    Collection<Review> getAll(Long filmId, Integer count);
    void addLike(Long reviewId, User userId);
    void addDislike(Long reviewId, User userId);
    void removeLike(Long reviewId, User userId);
    void removeDislike(Long reviewId, User userId);
}
