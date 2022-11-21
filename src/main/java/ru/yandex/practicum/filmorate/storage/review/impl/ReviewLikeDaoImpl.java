package ru.yandex.practicum.filmorate.storage.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeDao;

@Repository
@RequiredArgsConstructor
public class ReviewLikeDaoImpl implements ReviewLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public long getUseful(long reviewId) {
        int result = 0;
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
    public void addLike(long reviewId, long userId) {
        if (contains(reviewId, userId, true)) {
            return;
        }
        String sqlQuery = "INSERT INTO review_like (review_id, user_id, is_positive) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, true);
    }

    @Override
    public void addDislike(long reviewId, long userId) {
        if (contains(reviewId, userId, false)) {
            return;
        }
        String sqlQuery = "INSERT INTO review_like (review_id, user_id, is_positive) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, false);
    }

    @Override
    public void removeLike(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM review_like WHERE review_id = ? AND user_id = ? AND is_positive = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId, true);
    }

    @Override
    public void removeDislike(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM review_like WHERE review_id = ? AND user_id = ? AND is_positive = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId, false);
    }

    private boolean contains(long reviewId, long userId, boolean positive) {
        String sqlQuery = "SELECT * FROM review_like " +
                "WHERE review_id = ? AND user_id = ? AND is_positive = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, reviewId, userId, positive).next();
    }
}
