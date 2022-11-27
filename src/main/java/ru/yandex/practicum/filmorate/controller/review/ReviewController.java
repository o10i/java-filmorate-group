package ru.yandex.practicum.filmorate.controller.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@Positive @PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@Positive @PathVariable("reviewId") Long reviewId) {
        return reviewService.findById(reviewId);
    }

    @GetMapping
    public Collection<Review> getAllReviews(@RequestParam(name = "filmId", required = false) Optional<Long> filmId,
                                            @RequestParam(name = "count", required = false) Optional<Integer> count) {
        return reviewService.findAll(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addReviewLike(@Positive @PathVariable("reviewId") Long reviewId,
                              @Positive @PathVariable("userId") Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addReviewDislike(@Positive @PathVariable("reviewId") Long reviewId,
                                 @Positive @PathVariable("userId") Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeReviewLike(@Positive @PathVariable("reviewId") Long reviewId,
                                 @Positive @PathVariable("userId") Long userId) {
        reviewService.removeLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeReviewDislike(@Positive @PathVariable("reviewId") Long reviewId,
                                    @Positive @PathVariable("userId") Long userId) {
        reviewService.removeDislike(reviewId, userId);
    }
}
