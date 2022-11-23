package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;

import java.util.Collection;

public interface ReviewStorage {
    Review createReview(Review review);
    Review updateReview(Review review);
    Review findReviewById(Long reviewId);
    void deleteReview(Long reviewId);
    Collection<Review> findAllReviews();
    Collection<Review> findAllReviews(Long filmId, Integer count);
}
