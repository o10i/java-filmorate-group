package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;

    public Review create(Review review) {
        filmService.findFilmById(review.getFilmId());
        userService.findUserById(review.getUserId());
        reviewStorage.create(review);
        return reviewStorage.get(review.getId());
    }

    public Review update(Review review) {
        filmService.findFilmById(review.getFilmId());
        userService.findUserById(review.getUserId());
        reviewStorage.update(review);
        return reviewStorage.get(review.getId());
    }

    public void delete(Long reviewId) {
        reviewStorage.delete(reviewId);
    }

    public Review get(Long reviewId) {
        return reviewStorage.get(reviewId);
    }

    public Collection<Review> getAllReviews(Optional<Long> filmId, Optional<Integer> count) {
        if (filmId.isEmpty() && count.isEmpty()) {
            return reviewStorage.getAll().stream()
                    .sorted((o1, o2) -> Long.compare(o2.getUseful(), o1.getUseful()))
                    .collect(Collectors.toList());
        }
        return reviewStorage.getAll(filmId.orElse(-1L), count.orElse(10)).stream()
                .sorted((o1, o2) -> Long.compare(o2.getUseful(), o1.getUseful()))
                .collect(Collectors.toList());
    }

    public void addLike(Long reviewId, Long userId) {
        reviewStorage.addLike(reviewId, userService.findUserById(userId));
    }

    public void addDislike(Long reviewId, Long userId) {
        reviewStorage.addDislike(reviewId, userService.findUserById(userId));
    }

    public void removeLike(Long reviewId, Long userId) {
        reviewStorage.removeLike(reviewId, userService.findUserById(userId));
    }

    public void removeDislike(Long reviewId, Long userId) {
        reviewStorage.removeDislike(reviewId, userService.findUserById(userId));
    }
}
