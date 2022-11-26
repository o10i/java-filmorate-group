package ru.yandex.practicum.filmorate.storage.review.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewLikeDbStorage implements ReviewLikeStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public Long getUseful(Long reviewId) {
        long result = 0;
        String sqlQuery = "SELECT * FROM review_like WHERE review_id = ?";
        var rs = jdbcTemplate.queryForRowSet(sqlQuery, reviewId);
        while (rs.next()) {
            if (rs.getBoolean("is_positive")) {
                result++;
            } else {
                result--;
            }
        }
        return result;
    }

    @Override
    public void addLike(Review review, User user) {
        String sqlQuery = "INSERT INTO review_like (review_id, user_id, is_positive) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, review.getReviewId(), user.getId(), true);
    }

    @Override
    public void addDislike(Review review, User user) {
        String sqlQuery = "INSERT INTO review_like (review_id, user_id, is_positive) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, review.getReviewId(), user.getId(), false);
    }

    @Override
    public void removeLike(Review review, User user) {
        String sqlQuery = "DELETE FROM review_like WHERE review_id = ? AND user_id = ? AND is_positive = ?";
        jdbcTemplate.update(sqlQuery, review.getReviewId(), user.getId(), true);
    }

    @Override
    public void removeDislike(Review review, User user) {
        String sqlQuery = "DELETE FROM review_like WHERE review_id = ? AND user_id = ? AND is_positive = ?";
        jdbcTemplate.update(sqlQuery, review.getReviewId(), user.getId(), false);
    }

    @Override
    public boolean containsLike(Review review, User user, Boolean positive) {
        String sqlQuery = "SELECT * FROM review_like " +
                "WHERE review_id = ? AND user_id = ? AND is_positive = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, review.getReviewId(), user.getId(), positive).next();
    }
}
