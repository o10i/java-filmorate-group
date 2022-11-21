package ru.yandex.practicum.filmorate.storage.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.review.ReviewDao;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

@Repository
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewLikeDao reviewLikeDao;

    @Override
    public void create(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");
        review.setId(simpleJdbcInsert.executeAndReturnKey(review.toMap()).longValue());
    }

    @Override
    public void update(Review review) {
        get(review.getId());
        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, is_positive = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getPositive(),
                review.getId());
    }

    @Override
    public Review get(long reviewId) {
        String sqlQuery = "SELECT * FROM reviews WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, reviewId);
        } catch (DataAccessException e) {
            throw new DataNotFoundException("Review not found.");
        }
    }

    @Override
    public Collection<Review> getAll() {
        String sqlQuery = "SELECT * FROM reviews";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview);
    }

    @Override
    public void delete(long reviewId) {
        String sqlQuery = "DELETE FROM reviews WHERE id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    @Override
    public Collection<Review> getList(long filmId, int count) {
        if (filmId == -1) {
            String sqlQuery = "SELECT * FROM reviews LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToReview, count);
        }
        String sqlQuery = "SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToReview, filmId, count));
    }

    @Override
    public void addLike(long reviewId, User user) {
        get(reviewId);
        reviewLikeDao.addLike(reviewId, user.getId());
    }

    @Override
    public void addDislike(long reviewId, User user) {
        get(reviewId);
        reviewLikeDao.addDislike(reviewId, user.getId());
    }

    @Override
    public void removeLike(long reviewId, User user) {
        get(reviewId);
        reviewLikeDao.removeLike(reviewId, user.getId());
    }

    @Override
    public void removeDislike(long reviewId, User user) {
        get(reviewId);
        reviewLikeDao.removeDislike(reviewId, user.getId());
    }

    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder().id(rs.getLong("id"))
                .content(rs.getString("content"))
                .positive(rs.getBoolean("is_positive"))
                .filmId(rs.getLong("film_id"))
                .userId(rs.getLong("user_id"))
                .useful(reviewLikeDao.getUseful(rs.getLong("id")))
                .build();
    }
}
