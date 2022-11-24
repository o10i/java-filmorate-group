package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.follow.FollowDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.user.User;


import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    private final FollowDbStorage followDbStorage;


    private User getUser() {
        return User.builder().email("test@mail.ru").login("testLogin").name("testName").birthday(Date.valueOf("1946-08-20").toLocalDate()).build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FOLLOW");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void testDeleteUser() {
        User user = getUser();
        userDbStorage.create(user);
        userDbStorage.deleteUserById(1L);
        assertThrows(UserNotFoundException.class, ()-> userDbStorage.findUserById(1L));
    }

    @Test
    void testDeleteWrongUser() {
        User user = getUser();
        userDbStorage.create(user);
        assertThrows(UserNotFoundException.class, ()-> userDbStorage.deleteUserById(333L));
    }

    @Test
    void testFollowAfterDeleting() {
        User user = getUser();
        User friend = getUser();
        friend.setEmail("friend@email.ru");
        friend.setName("friend");
        friend.setLogin("friendLogin");
        userDbStorage.create(user);
        userDbStorage.create(friend);
        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(2L, 2L);
        assertEquals(1, followDbStorage.getAllFriends(1L).size());
        userDbStorage.deleteUserById(2L);
        assertEquals(0, followDbStorage.getAllFriends(1L).size());


    }

}
