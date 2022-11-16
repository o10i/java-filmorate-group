package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;


    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(Long userId, Long filmId) {
        String sqlQuery = "MERGE INTO LIKES key(FILM_ID,USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void deleteLike(Long userId, Long filmId) {
        String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public int LikeFromUser(Long filmId, Long userId) {
        String sqlQuery = "SELECT COUNT(user_id) FROM likes WHERE film_id = ? AND user_id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);
    }
}
