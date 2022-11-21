package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewDao;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    public Review create(Review review) {
        validate(review);
        reviewDao.create(review);
        return reviewDao.get(review.getId());
    }

    public Review update(Review review) {
        validate(review);
        reviewDao.update(review);
        return reviewDao.get(review.getId());
    }

    public void delete(long reviewId) {
        reviewDao.delete(reviewId);
    }

    public Review get(long reviewId) {
        return reviewDao.get(reviewId);
    }

    public Collection<Review> getAll() {
        return reviewDao.getAll().stream()
                .sorted((o1, o2) -> Long.compare(o2.getUseful(), o1.getUseful()))
                .collect(Collectors.toList());
    }

    public Collection<Review> getList(long filmId, int count) {
        return reviewDao.getList(filmId, count).stream()
                .sorted((o1, o2) -> Long.compare(o2.getUseful(), o1.getUseful()))
                .collect(Collectors.toList());
    }

    public void addLike(long reviewId, long userId) {
        reviewDao.addLike(reviewId, userDbStorage.findUserById(userId));
    }

    public void addDislike(long reviewId, long userId) {
        reviewDao.addDislike(reviewId, userDbStorage.findUserById(userId));
    }

    public void removeLike(long reviewId, long userId) {
        reviewDao.removeLike(reviewId, userDbStorage.findUserById(userId));
    }

    public void removeDislike(long reviewId, long userId) {
        reviewDao.removeDislike(reviewId, userDbStorage.findUserById(userId));
    }

    private void validate(Review review) {
        if (!StringUtils.hasText(review.getContent())) {
            throw new ValidationException("Review has no content.");
        }
        if (review.getFilmId() == null) {
            throw new ValidationException("Incorrect film id.");
        }
        if (review.getUserId() == null) {
            throw new ValidationException("Incorrect user id.");
        }
        if (review.getPositive() == null) {
            throw new ValidationException("Incorrect is_positive.");
        }
        userDbStorage.findUserById(review.getUserId());
        filmDbStorage.findFilmById(review.getFilmId());
    }
}
