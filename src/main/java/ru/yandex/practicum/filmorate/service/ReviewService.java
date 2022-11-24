package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewLikeStorage likeStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final FeedService feedService;

    public Review createReview(Review review) {
        filmService.findFilmById(review.getFilmId());
        userService.findUserById(review.getUserId());
        Review reviewToEvent = reviewStorage.createReview(review);
        feedService.saveEvent(Event.createEvent(reviewToEvent.getUserId(), EventType.REVIEW, Operation.ADD
                , reviewToEvent.getReviewId()));
        return reviewToEvent;
    }

    public Review updateReview(Review review) {
        filmService.findFilmById(review.getFilmId());
        userService.findUserById(review.getUserId());
        Review reviewToEvent = findReviewById(review.getReviewId());
        feedService.saveEvent(Event.createEvent(reviewToEvent.getUserId(), EventType.REVIEW, Operation.UPDATE
                , reviewToEvent.getReviewId()));
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(Long reviewId) {
        Review reviewToEvent = findReviewById(reviewId);
        reviewStorage.deleteReview(reviewId);
        feedService.saveEvent(Event.createEvent(reviewToEvent.getUserId(), EventType.REVIEW, Operation.REMOVE
                , reviewId));
    }

    public Review findReviewById(Long reviewId) {
        return reviewStorage.findReviewById(reviewId);
    }

    public Collection<Review> findAllReviews(Optional<Long> filmId, Optional<Integer> count) {
        if (filmId.isEmpty() && count.isEmpty()) {
            return reviewStorage.findAllReviews().stream()
                    .sorted((o1, o2) -> Long.compare(o2.getUseful(), o1.getUseful()))
                    .collect(Collectors.toList());
        }
        return reviewStorage.findAllReviews(filmId.orElse(-1L), count.orElse(10)).stream()
                .sorted((o1, o2) -> Long.compare(o2.getUseful(), o1.getUseful()))
                .collect(Collectors.toList());
    }

    public void addLike(Long reviewId, Long userId) {
        var review = findReviewById(reviewId);
        var user = userService.findUserById(userId);
        if (likeStorage.containsLike(review, user, true)) {
            return;
        }
        likeStorage.addLike(review, user);
    }

    public void addDislike(Long reviewId, Long userId) {
        var review = findReviewById(reviewId);
        var user = userService.findUserById(userId);
        if (likeStorage.containsLike(review, user, false)) {
            return;
        }
        likeStorage.addDislike(review, user);
    }

    public void removeLike(Long reviewId, Long userId) {
        var review = findReviewById(reviewId);
        var user = userService.findUserById(userId);
        if (!likeStorage.containsLike(review, user, true)) {
            return;
        }
        likeStorage.removeLike(review, user);
    }

    public void removeDislike(Long reviewId, Long userId) {
        var review = findReviewById(reviewId);
        var user = userService.findUserById(userId);
        if (!likeStorage.containsLike(review, user, false)) {
            return;
        }
        likeStorage.removeDislike(review, user);
    }
}
