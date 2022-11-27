package ru.yandex.practicum.filmorate.service.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewService {
    ReviewStorage reviewStorage;
    ReviewLikeStorage likeStorage;
    UserService userService;
    FilmService filmService;
    FeedService feedService;

    public Review create(Review review) {
        filmService.getById(review.getFilmId());
        userService.getById(review.getUserId());
        Review reviewToEvent = reviewStorage.create(review);
        feedService.saveEvent(Event.createEvent(reviewToEvent.getUserId(), EventType.REVIEW, Operation.ADD,
                reviewToEvent.getReviewId()));
        return reviewToEvent;
    }

    public Review update(Review review) {
        feedService.saveEvent(Event.createEvent(getById(review.getReviewId()).getUserId(), EventType.REVIEW, Operation.UPDATE,
                review.getReviewId()));
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        feedService.saveEvent(Event.createEvent(getById(reviewId).getUserId(), EventType.REVIEW, Operation.REMOVE,
                reviewId));
        reviewStorage.delete(reviewId);
    }

    public Review getById(Long reviewId) {
        return reviewStorage.getById(reviewId);
    }

    public Collection<Review> getAll(Optional<Long> filmId, Optional<Integer> count) {
        if (filmId.isEmpty() && count.isEmpty()) {
            return reviewStorage.getAll();
        }
        return reviewStorage.getAll(filmId.orElse(-1L), count.orElse(10));
    }

    public void addLike(Long reviewId, Long userId) {
        likeStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        likeStorage.addDislike(reviewId, userId);
    }

    public void removeLike(Long reviewId, Long userId) {
        likeStorage.removeLike(reviewId, userId);
    }

    public void removeDislike(Long reviewId, Long userId) {
        likeStorage.removeDislike(reviewId, userId);
    }
}
