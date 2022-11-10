package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sqlQueryForFilm = "SELECT * FROM MOVIE GROUP BY ID";
        List<Film> films = jdbcTemplate.query(sqlQueryForFilm, this::mapRowToFilm);

        List<Film> filmsWithGenre = new ArrayList<>();
        for (Film film: films) {
            String sqlQueryForGenre = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?";
            List<Genre> genres = jdbcTemplate.query(sqlQueryForGenre, this::mapRowToGenre, film.getId());
            filmsWithGenre.add(Film.builder()
                            .name(film.getName())
                            .description(film.getDescription())
                            .releaseDate(film.getReleaseDate())
                            .duration(film.getDuration())
                            .rate(film.getRate())
                            .genres(new HashSet<>(genres))
                    .build());
        }
        return filmsWithGenre;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("movie")
                .usingGeneratedKeyColumns("id");
       // System.out.println(film.toString());
        System.out.println(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        System.out.println(findFilmById(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue()));
        return findFilmById(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
    }

    @Override
    public Film update(Film film) {
        String sqlQueryForFilm = "UPDATE MOVIE SET " +
                "NAME = ?, DESCRIPTION = ?, REALEASE_DATE = ? , DURATION = ?, RATE = ?, MPA = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQueryForFilm
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId());
        if (film.getGenres() != null) {
            String sqlQueryForClean = "DELETE FROM FILM_GENRE WHERE FILM_ID + ?";
            jdbcTemplate.update(sqlQueryForClean, film.getId());
            for (Genre genre: film.getGenres()) {
                String sqlQueryForGenre = "MERGE INTO FILM_GENRE KEY(FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForGenre
                        , film.getId()
                        , genre.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(Long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM MOVIE WHERE id = ?", filmId);
        System.out.println(filmRows.toString());
        if(filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("releaseDate").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .rate(filmRows. getInt("rate"))
                    //.mpa((MPA) filmRows.getObject("MPA"))
                    .build();
            return film;
        } else {
           return null;
        }
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        String sqlQuery = "INSERT INTO LIKES(film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT m.name, m.description, m.releaseDate, m.duration " +
                          "FROM MOVIE AS m " +
                          "JOIN likes AS l ON m.id = l.film_id " +
                          "GROUP BY m.id " +
                          "ORDER BY COUNT(l.user_id) DESC " +
                          "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .rate(resultSet.getInt("rate"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .build();
    }


}
