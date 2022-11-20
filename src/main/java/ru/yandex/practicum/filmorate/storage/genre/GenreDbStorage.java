package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAllGenre() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToGenre(rs));
    }

    public Genre findGenreById(Long genreId) {
        String sqlQuery = "SELECT * FROM GENRE WHERE id = ?";
        return jdbcTemplate.query(sqlQuery,(rs, rowNum) -> mapRowToGenre(rs), genreId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(String.format("Genre with %d id not found", genreId)));
    }

    public void addFilmsGenre (Long filmId, LinkedHashSet<Genre> genres) {
        List<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(
                "MERGE INTO film_genre key(FILM_ID,genre_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement statement, int i) throws SQLException {
                        statement.setLong(1, filmId);
                        statement.setLong(2, genreList.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genreList.size();
                    }
                }
        );
    }

    public void loadGenres(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        final String sqlQuery = "SELECT * FROM GENRE AS g, FILM_GENRE AS fg WHERE fg.GENRE_ID = g.ID AND fg.FILM_ID in ("
                + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getLong("FILM_ID"));
            film.getGenres().add(mapRowToGenre(rs));
        }, films.stream().map(Film::getId).toArray());
    }
    public void deleteFilmsGenre(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID + ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public static Genre mapRowToGenre(ResultSet resultSet) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
