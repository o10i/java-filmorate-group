package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;


@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final GenreDbStorage genreDbStorage;


    private final MpaDbStorage mpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public List<Film> findAll() {
        String sqlQueryForFilm = "SELECT * FROM MOVIE AS m " +
                "LEFT JOIN MPA ON m.id = MPA.id " +
                "GROUP BY ID";

        return jdbcTemplate.query(sqlQueryForFilm, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("movie")
                .usingGeneratedKeyColumns("id");
        if(film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                genreDbStorage.addFilmsGenre(film.getId(), genre.getId());
            }
        }
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
            genreDbStorage.deleteFilmsGenre(film.getId());
            for (Genre genre: film.getGenres()) {
                genreDbStorage.addFilmsGenre(film.getId(), genre.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(Long filmId) {
        /*String sqlQuery = "SELECT * FROM MOVIE AS m " +
                "LEFT JOIN MPA ON m.id = MPA.id " +
                "WHERE m.id = ?";
         jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);*/
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM MOVIE AS m LEFT JOIN MPA ON m.id = MPA.id " +
                "WHERE m.id = ?", filmId);

        if(userRows.next()) {
            Film film = Film.builder()
                    .id(userRows.getLong("id"))
                    .name(userRows.getString("name"))
                    .description(userRows.getString("description"))
                    .releaseDate(userRows.getDate("release_date").toLocalDate())
                    .duration(userRows.getInt("duration"))
                    .rate(userRows.getInt("rate"))
                    .mpa(userRows.getObject("MPA", Mpa.class))
                    .build();
            return film;
        } else {
            return null;
        }
    }
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT * " +
                "FROM MOVIE AS m " +
                "INNER JOIN likes AS l ON m.id = l.film_id " +
                "LEFT JOIN MPA ON m.id = MPA.id " +
                "GROUP BY m.id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .rate(resultSet.getInt("rate"))
                .mpa(mpaDbStorage.findMPAById(resultSet.getLong("mpa_id")))
                .genres(new HashSet<>(genreDbStorage.findFilmGenre(resultSet.getLong("id"))))
                .build();
    }
}
