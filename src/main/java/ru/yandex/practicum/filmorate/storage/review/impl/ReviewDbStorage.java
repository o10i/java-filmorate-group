package ru.yandex.practicum.filmorate.storage.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewLikeStorage reviewLikeStorage;

    @Override
    public Review createReview(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");
        review.setReviewId(simpleJdbcInsert.executeAndReturnKey(review.toMap()).longValue());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, is_positive = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getPositive(),
                review.getReviewId());
        return review;
    }

    @Override
    public Review findReviewById(Long reviewId) {
        String sqlQuery = "SELECT * FROM reviews WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, reviewId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("Review not found."));
    }

    @Override
    public Collection<Review> findAllReviews() {
        String sqlQuery = "SELECT * FROM reviews";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        String sqlQuery = "DELETE FROM reviews WHERE id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    @Override
    public Collection<Review> findAllReviews(Long filmId, Integer count) {
        if (filmId == -1) {
            String sqlQuery = "SELECT * FROM reviews LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToReview, count);
        }
        String sqlQuery = "SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToReview, filmId, count));
    }

    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder().reviewId(rs.getLong("id"))
                .content(rs.getString("content"))
                .positive(rs.getBoolean("is_positive"))
                .filmId(rs.getLong("film_id"))
                .userId(rs.getLong("user_id"))
                .useful(reviewLikeStorage.getUseful(rs.getLong("id")))
                .build();
    }
}
