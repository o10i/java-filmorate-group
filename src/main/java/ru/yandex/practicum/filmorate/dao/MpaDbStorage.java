package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> findAllMPA() {
        String sqlQuery = "SELECT * FROM RATING";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    public Mpa findMPAById(Long MPAId) {
        SqlRowSet MPARows = jdbcTemplate.queryForRowSet("SELECT * FROM RATING WHERE id = ?", MPAId);

        if(MPARows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(MPARows.getLong("id"))
                    .name(MPARows.getString("name"))
                    .build();
            return mpa;
        } else {
            return null;
        }
    }
    private Mpa mapRowToMPA(ResultSet resultSet, int nowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
