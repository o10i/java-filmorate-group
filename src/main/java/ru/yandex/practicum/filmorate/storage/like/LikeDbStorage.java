package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
public class LikeDbStorage implements LikeStorage {

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

    public List<Like> getLikes(Long userId, Long filmId) {
        String sqlQuery = "SELECT * FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToLike, userId, filmId);
    }

    private Like mapRowToLike(ResultSet resultSet, int nowNum) throws SQLException {
        return Like.builder()
                .filmId(resultSet.getLong("film_id"))
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}
