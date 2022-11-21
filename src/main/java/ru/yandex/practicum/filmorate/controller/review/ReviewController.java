package ru.yandex.practicum.filmorate.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return service.create(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        return service.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") int reviewId) {
        service.delete(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@PathVariable("reviewId") int reviewId) {
        return service.get(reviewId);
    }

    @GetMapping
    public Collection<Review> getReviewList(@RequestParam(name = "filmId", required = false) Optional<Long> filmId,
                                            @RequestParam(name = "count", required = false) Optional<Integer> count) {
        if (filmId.isEmpty() && count.isEmpty()) {
            return service.getAll();
        }
        return service.getList(filmId.orElse(-1L), count.orElse(10));
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addReviewLike(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId) {
        service.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addReviewDislike(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId) {
        service.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeReviewLike(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId) {
        service.removeLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeReviewDislike(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId) {
        service.removeDislike(reviewId, userId);
    }
}
