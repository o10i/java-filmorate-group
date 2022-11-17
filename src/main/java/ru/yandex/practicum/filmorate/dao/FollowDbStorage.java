package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.dao.UserDbStorage.mapRowToUser;

@Component
public class FollowDbStorage {

    private final JdbcTemplate jdbcTemplate;


    public FollowDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO FOLLOW(USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM FOLLOW WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getAllFriends(Long userId) {
        String sqlQuery = "SELECT * " +
                "FROM USERS AS U " +
                "INNER JOIN FOLLOW AS F ON U.ID = F.FRIEND_ID " +
                "WHERE F.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        String sqlQuery = "SELECT * FROM USERS AS U " +
                "INNER JOIN FOLLOW AS F ON U.ID = F.FRIEND_ID " +
                "WHERE USER_ID = ? AND FRIEND_ID IN (SELECT FRIEND_ID FROM FOLLOW WHERE USER_ID = ?) ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), userId, friendId);
    }
}
