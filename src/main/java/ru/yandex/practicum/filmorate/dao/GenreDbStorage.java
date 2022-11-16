package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAllGenre() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre findGenreById(Long genreId) {
        String sqlQuery = "SELECT * FROM GENRE WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, genreId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(String.format("Genre with %d id not found", genreId)));
    }

    public void addFilmsGenre (Long filmId, List<Genre> genres) {
        List<Genre> genresUniq = genres.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO films_genres (genre_id, film_id) VALUES (?, ?);",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement statement, int i) throws SQLException {
                        statement.setLong(1, genresUniq.get(i).getId());
                        statement.setLong(2, filmId);
                    }
                    public int getBatchSize() {
                        return genresUniq.size();
                    }
                }
        );
    }
    public void deleteFilmsGenre(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID + ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public List <Genre> findFilmGenre (Long filmId) {
        String sqlQuery = "SELECT * FROM genre as g " +
                "INNER JOIN film_genre as fg ON g.id = fg.genre_id " +
                "WHERE fg.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }
    private Genre mapRowToGenre(ResultSet resultSet, int nowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
