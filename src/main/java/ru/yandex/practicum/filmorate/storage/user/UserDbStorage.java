package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.film.FilmDbStorage.mapRowToFilm;

@Repository("userStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs));
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, NAME = ? , BIRTHDAY = ?" +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE id = ?";

        return jdbcTemplate.query(sqlQuery,(rs, rowNum) -> mapRowToUser(rs), userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User with %d id not found", userId)));
    }

    @Override
    public List<Film> getRecommendations(Long userId) {
        List<Film> recommendations = new ArrayList<>();

        String sqlQueryForSimilarUser = "SELECT user_id, COUNT(*) AS same_film_count " +
                            "FROM likes " +
                            "WHERE film_id IN (SELECT film_id " +
                                        "FROM likes " +
                                        "WHERE user_id = ?) AND user_id != ? " +
                            "GROUP BY user_id " +
                            "ORDER BY same_film_count DESC " +
                            "LIMIT 1";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQueryForSimilarUser, userId, userId);

        if (!rowSet.next()) {
            return recommendations;
        }

        Long similarUserId = rowSet.getLong("user_id");

        String sqlQueryForRecommendations = "SELECT * " +
                            "FROM MOVIE AS m " +
                            "INNER JOIN MPA ON MPA.id = m.mpa_id " +
                            "LEFT JOIN likes AS l ON l.film_id = m.id " +
                            "WHERE l.film_id NOT IN (SELECT film_id " +
                                            "FROM likes " +
                                            "WHERE user_id = ?) AND l.user_id = ? ";

        return jdbcTemplate.query(sqlQueryForRecommendations,
                (rs, rowNum) -> mapRowToFilm(rs),
                userId, similarUserId);
    }

    public static User mapRowToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
