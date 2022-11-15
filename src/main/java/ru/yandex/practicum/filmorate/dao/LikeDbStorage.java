package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;




    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(Long userId, Long filmId) {
        String sqlQuery = "INSERT INTO LIKES(film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    public void deleteLike(Long userId, Long filmId) {
        String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }



    public List <Like> findFilmLike (Long filmId) {
        String sqlQuery = "SELECT user_id FROM likes WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToLike, filmId);
    }


    public Like mapRowToLike(ResultSet resultSet, int rowNum) throws SQLException {
        return Like.builder()
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}
