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
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToGenre(rs));
    }

    public Genre findGenreById(Long genreId) {
        String sqlQuery = "SELECT * FROM GENRE WHERE id = ?";
        return jdbcTemplate.query(sqlQuery,(rs, rowNum) -> mapRowToGenre(rs), genreId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(String.format("Genre with %d id not found", genreId)));
    }

    public void addFilmsGenre (Long filmId, List<Genre> genres) {
        List<Genre> genresUniq = genres.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "MERGE INTO film_genre key(FILM_ID,genre_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement statement, int i) throws SQLException {
                        statement.setLong(1, filmId);
                        statement.setLong(2, genresUniq.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genresUniq.size();
                    }
                }
        );
    }

   /* public void addGenres (List<Film> films) {
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        List<Film> filmsWith = jdbcTemplate.query(
                String.format("SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID IN (%s)", inSql),
                filmById.values().toArray(),
                (rs, rowNum) -> Film.builder()
                        .id(rs.getLong("id"))
                        .name((rs.getString("name")))
                        .releaseDate((rs.getDate("release_date")).toLocalDate())
                        .description(rs.getString("description"))
                        .duration(rs.getInt("duration"))
                        .rate(rs.getInt("rate"))
                        .mpa(Mpa.builder()
                                .id(rs.getLong("mpa.id"))
                                .name(rs.getString("mpa.name"))
                                .build())
                        .genres((rs.getLong("id")))
                        .build();
    }*/
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
