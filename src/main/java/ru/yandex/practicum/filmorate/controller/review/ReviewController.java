package ru.yandex.practicum.filmorate.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        return service.create(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return service.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@Positive @PathVariable("reviewId") Long reviewId) {
        service.delete(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@Positive @PathVariable("reviewId") Long reviewId) {
        return service.get(reviewId);
    }

    @GetMapping
    public Collection<Review> getAllReviews(@RequestParam(name = "filmId", required = false) Optional<Long> filmId,
                                            @RequestParam(name = "count", required = false) Optional<Integer> count) {
        return service.getAllReviews(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addReviewLike(@Positive @PathVariable("reviewId") Long reviewId,
                              @Positive @PathVariable("userId") Long userId) {
        service.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addReviewDislike(@Positive @PathVariable("reviewId") Long reviewId,
                                 @Positive @PathVariable("userId") Long userId) {
        service.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeReviewLike(@Positive @PathVariable("reviewId") Long reviewId,
                                 @Positive @PathVariable("userId") Long userId) {
        service.removeLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeReviewDislike(@Positive @PathVariable("reviewId") Long reviewId,
                                    @Positive @PathVariable("userId") Long userId) {
        service.removeDislike(reviewId, userId);
    }
}
