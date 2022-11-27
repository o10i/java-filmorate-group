package ru.yandex.practicum.filmorate.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Create;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Update;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Validated(Create.class) @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Validated(Update.class) @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void delete(@Positive @PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getById(@Positive @PathVariable("reviewId") Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @GetMapping
    public Collection<Review> getAll(@RequestParam(name = "filmId", required = false) Optional<Long> filmId,
                                     @RequestParam(name = "count", required = false) Optional<Integer> count) {
        return reviewService.getAll(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@Positive @PathVariable("reviewId") Long reviewId,
                        @Positive @PathVariable("userId") Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@Positive @PathVariable("reviewId") Long reviewId,
                           @Positive @PathVariable("userId") Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@Positive @PathVariable("reviewId") Long reviewId,
                           @Positive @PathVariable("userId") Long userId) {
        reviewService.removeLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@Positive @PathVariable("reviewId") Long reviewId,
                              @Positive @PathVariable("userId") Long userId) {
        reviewService.removeDislike(reviewId, userId);
    }
}
