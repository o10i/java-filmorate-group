package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository("filmStorage")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    public static Film mapRowToFilm(ResultSet resultSet) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .releaseDate((resultSet.getDate("release_date")).toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa.id"))
                        .name(resultSet.getString("mpa.name"))
                        .build())
                .genres(new LinkedHashSet<>())
                .directors(new LinkedHashSet<>())
                .build();
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM MOVIE AS m " + "INNER JOIN MPA ON MPA.id = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs));
    }

    @Override
    public Film getById(Long filmId) {
        String sqlQuery = "SELECT * FROM MOVIE AS m " +
                "INNER JOIN MPA ON m.mpa_id = MPA.id " +
                "WHERE m.id = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), filmId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Film with %d id not found", filmId)));

    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO movie(name, description, release_date, duration, rate,  mpa_id) " + "VALUES (?, ?, ?, ?, ?, ?)";
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
        String sqlQuery = "UPDATE MOVIE SET " + "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ?, RATE = ?, MPA_ID = ? " + "WHERE ID = ?";

        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId());

        return film;
    }

    @Override
    public void deleteById(Long filmId) {
        String sqlQuery = "DELETE FROM movie WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, filmId) == 0) {
            throw new ObjectNotFoundException(String.format("Film with %d id not found", filmId));
        }
    }

    @Override
    public List<Film> getTop(Integer count, Optional<Integer> genreId, Optional<Integer> year) {
        String sqlQuery;
        if (genreId.isEmpty() && year.isEmpty()) {
            sqlQuery = "SELECT * " +
                    "FROM MOVIE M " +
                    "INNER JOIN MPA ON MPA.ID = M.MPA_ID " +
                    "LEFT JOIN LIKES L ON L.FILM_ID = M.ID " +
                    "GROUP BY M.ID, L.USER_ID " +
                    "ORDER BY COUNT(L.USER_ID) DESC " +
                    "LIMIT ?";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), count);
        } else if (genreId.isPresent() && year.isEmpty()) {
            sqlQuery = "SELECT * " +
                    "FROM MOVIE M " +
                    "INNER JOIN MPA ON M.MPA_ID = MPA.ID " +
                    "LEFT JOIN LIKES L ON M.ID = L.FILM_ID " +
                    "LEFT JOIN FILM_GENRE FG ON M.ID = FG.FILM_ID " +
                    "WHERE FG.GENRE_ID = ? " +
                    "GROUP BY M.ID, L.USER_ID " +
                    "ORDER BY COUNT(L.USER_ID) DESC " +
                    "LIMIT ?";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), genreId.get(), count);
        } else if (genreId.isEmpty()) {
            sqlQuery = "SELECT * " +
                    "FROM MOVIE M " +
                    "INNER JOIN MPA ON M.MPA_ID = MPA.ID " +
                    "LEFT JOIN LIKES L ON M.ID = L.FILM_ID " +
                    "WHERE YEAR(M.RELEASE_DATE) = ? " +
                    "GROUP BY M.ID, L.USER_ID " +
                    "ORDER BY COUNT(L.USER_ID) DESC " +
                    "LIMIT ?";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), year.get(), count);
        }
        sqlQuery = "SELECT * " +
                "FROM MOVIE M " +
                "INNER JOIN MPA ON M.MPA_ID = MPA.ID " +
                "LEFT JOIN LIKES L ON M.ID = L.FILM_ID " +
                "LEFT JOIN FILM_GENRE FG ON M.ID = FG.FILM_ID " +
                "WHERE FG.GENRE_ID = ? AND YEAR(M.RELEASE_DATE) = ? " +
                "GROUP BY M.ID, L.USER_ID " +
                "ORDER BY COUNT(L.USER_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), genreId.get(), year.get(), count);
    }

    @Override
    public List<Film> getCommon(Long userId, Long friendId) {
        String check = "SELECT name FROM users WHERE id = ?";
        try {
            jdbcTemplate.queryForObject(check, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("User with id %d not found", userId));
        }
        try {
            jdbcTemplate.queryForObject(check, String.class, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("User with id %d not found", friendId));
        }
        String sqlQuery = "SELECT m.*,mpa.id,mpa.name FROM movie m, likes l1, likes l2 " +
                "INNER JOIN MPA ON (MPA.id = m.mpa_id)" +
                "WHERE l1.user_id = ? AND l2.user_id = ? " +
                "AND m.id = l1.film_id AND m.id = l2.film_id " +
                "GROUP BY m.id " +
                "ORDER BY COUNT(l1.user_id) DESC";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), userId, friendId);
    }

    @Override
    public List<Film> getDirectorFilmsBy(Long directorId, DirectorSortBy filmSortBy) {
        String sqlQuery;
        if (filmSortBy.equals(DirectorSortBy.year)) {
            sqlQuery = "SELECT * " +
                    "FROM MOVIE M " +
                    "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                    "WHERE M.ID IN (" +
                    "SELECT FILM_ID " +
                    "FROM FILM_DIRECTOR " +
                    "WHERE DIRECTOR_ID = ?) " +
                    "ORDER BY M.RELEASE_DATE";
        } else {
            sqlQuery = "SELECT * " +
                    "FROM MOVIE M " +
                    "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                    "LEFT JOIN LIKES L ON L.FILM_ID = M.ID " +
                    "WHERE M.ID IN (" +
                    "SELECT FILM_ID " +
                    "FROM FILM_DIRECTOR " +
                    "WHERE DIRECTOR_ID = ?) " +
                    "GROUP BY M.ID " +
                    "ORDER BY COUNT(L.USER_ID) " +
                    "DESC";
        }
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), directorId);
    }

    @Override
    public List<Film> searchTopFilmsBy(String query, boolean searchByFilmName, boolean searchByDirectorName) {
        String search = "%" + query.toLowerCase() + "%";
        String sqlQuery;
        if (searchByFilmName && searchByDirectorName) {
            sqlQuery = "SELECT M.*, MPA.* " +
                    "FROM MOVIE M " +
                    "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                    "LEFT JOIN LIKES L ON M.ID = L.FILM_ID " +
                    "LEFT JOIN FILM_DIRECTOR FD on M.ID = FD.FILM_ID " +
                    "LEFT JOIN DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
                    "WHERE LOWER(D.NAME) LIKE ? OR LOWER(M.NAME) LIKE ? " +
                    "GROUP BY M.id " +
                    "ORDER BY COUNT(L.USER_ID) DESC";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), search, search);
        } else if (searchByFilmName) {
            sqlQuery = "SELECT M.*, MPA.* " +
                    "FROM MOVIE M " +
                    "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                    "LEFT JOIN LIKES L ON M.ID = L.FILM_ID " +
                    "WHERE LOWER(M.NAME) LIKE ? " +
                    "GROUP BY M.id " +
                    "ORDER BY COUNT(L.USER_ID) DESC;";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), search);
        } else if (searchByDirectorName) {
            sqlQuery = "SELECT M.*, MPA.* " +
                    "FROM LIKES L " +
                    "LEFT JOIN MOVIE M ON M.ID = L.FILM_ID " +
                    "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                    "LEFT JOIN FILM_DIRECTOR FD on M.ID = FD.FILM_ID " +
                    "LEFT JOIN DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
                    "WHERE LOWER(D.NAME) LIKE ? " +
                    "GROUP BY M.id " +
                    "ORDER BY COUNT(L.USER_ID) DESC";
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs), search);
        }
        throw new IllegalArgumentException("Некорректный тип поиска");
    }
}
