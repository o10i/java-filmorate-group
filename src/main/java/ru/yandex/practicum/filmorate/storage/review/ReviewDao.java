package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

public interface ReviewDao {
    void create(Review review);
    void update(Review review);
    Review get(long reviewId);
    void delete(long reviewId);
    Collection<Review> getAll();
    Collection<Review> getList(long filmId, int count);
    void addLike(long reviewId, User userId);
    void addDislike(long reviewId, User userId);
    void removeLike(long reviewId, User userId);
    void removeDislike(long reviewId, User userId);
}
