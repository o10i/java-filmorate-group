package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final GenreDbStorage genreDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM MOVIE AS m " +
                "INNER JOIN MPA ON MPA.id = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO movie(name, description, release_date, duration, rate,  mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE MOVIE SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ?, RATE = ?, MPA_ID = ? " +
                "WHERE ID = ?";

        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId());

        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(Long filmId) {
        String sqlQuery = "SELECT * FROM MOVIE AS m " +
                "INNER JOIN MPA ON m.mpa_id = MPA.id " +
                "WHERE m.id = ?";

        return jdbcTemplate.query(sqlQuery,this::mapRowToFilm, filmId)
                .stream()
                .findAny()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with %d id not found", filmId)));

    }
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT * " +
                "FROM MOVIE AS m " +
                "INNER JOIN likes AS l ON m.id = l.film_id " +
                "INNER JOIN MPA ON m.mpa_id = MPA.id " +
                "GROUP BY m.id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .releaseDate((resultSet.getDate("releaseDate")).toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa.id"))
                        .name(resultSet.getString("mpa.name"))
                        .build())
                .genres(genreDbStorage.findFilmGenre(resultSet.getLong("id")))
                .build();
    }
}
