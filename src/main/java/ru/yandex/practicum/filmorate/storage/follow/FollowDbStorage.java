package ru.yandex.practicum.filmorate.storage.follow;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.user.UserDbStorage.mapRowToUser;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowDbStorage implements FollowStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO FOLLOW(USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM FOLLOW WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        String sqlQuery = "SELECT * " +
                "FROM USERS AS U " +
                "INNER JOIN FOLLOW AS F ON U.ID = F.FRIEND_ID " +
                "WHERE F.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        String sqlQuery = "SELECT * FROM USERS AS U " +
                "INNER JOIN FOLLOW AS F ON U.ID = F.FRIEND_ID " +
                "WHERE USER_ID = ? AND FRIEND_ID IN (SELECT FRIEND_ID FROM FOLLOW WHERE USER_ID = ?) ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), userId, friendId);
    }
}
