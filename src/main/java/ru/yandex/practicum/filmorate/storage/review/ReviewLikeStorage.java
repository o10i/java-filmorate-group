package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;

public interface ReviewLikeStorage {
    Long getUseful(Long reviewId);
    void addLike(Review review, User user);
    void addDislike(Review review, User user);
    void removeLike(Review review, User user);
    void removeDislike(Review review, User user);
    boolean containsLike(Review review, User user, Boolean positive);
}
