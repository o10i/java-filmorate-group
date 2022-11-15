package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
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
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE id = ?", genreId);

        if(genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getLong("id"))
                    .name(genreRows.getString("name"))
                    .build();
            return genre;
        } else {
            return null;
        }
    }

    public void addFilmsGenre (Long filmId, Long genreId) {
        String sqlQuery = "MERGE INTO FILM_GENRE KEY(FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }
    public void deleteFilmsGenre(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID + ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public List <Genre> findFilmGenre (Long filmId) {
        String sqlQuery = "SELECT * FROM genre as g " +
                "INNER JOIN film_genre as fg ON g.id = fg.genre_id " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }
    private Genre mapRowToGenre(ResultSet resultSet, int nowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
