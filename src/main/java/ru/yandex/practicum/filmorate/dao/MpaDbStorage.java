package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> findAllMPA() {
        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    public Mpa findMPAById(Long mpaId) {
        String sqlQuery = "SELECT * FROM MPA WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA, mpaId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(String.format("MPA with %d id not found", mpaId)));
    }
    private Mpa mapRowToMPA(ResultSet resultSet, int nowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
