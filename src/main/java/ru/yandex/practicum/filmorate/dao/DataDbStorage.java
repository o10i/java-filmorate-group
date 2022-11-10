package ru.yandex.practicum.filmorate.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DataDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public DataDbStorage(JdbcTemplate jdbcTemplate) {
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

    public List<MPA> findAllMPA() {
        String sqlQuery = "SELECT * FROM RATING";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    public MPA findMPAById(Long MPAId) {
        SqlRowSet MPARows = jdbcTemplate.queryForRowSet("SELECT * FROM RATING WHERE id = ?", MPAId);

        if(MPARows.next()) {
            MPA mpa = MPA.builder()
                    .id(MPARows.getLong("id"))
                    .name(MPARows.getString("name"))
                    .build();
            return mpa;
        } else {
            return null;
        }
    }


    private Genre mapRowToGenre(ResultSet resultSet, int nowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private MPA mapRowToMPA(ResultSet resultSet, int nowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }


}
