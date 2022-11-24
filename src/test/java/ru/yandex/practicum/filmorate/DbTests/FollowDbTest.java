package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.follow.FollowDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FollowDbTest {
    private final JdbcTemplate jdbcTemplate;
    private final FollowDbStorage followDbStorage;
    private final UserDbStorage userDbStorage;

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
    public void testAddFriend() {
        userDbStorage.createUser(getUser());
        User user = getUser();
        user.setEmail("test1@mail.ru");
        user.setLogin("testLogin1");
        userDbStorage.createUser(user);
        assertDoesNotThrow(() -> followDbStorage.addFriend(1L, 2L));
    }

    @Test
    void testAddUnknownFriend() {
        userDbStorage.createUser(getUser());
        assertThrows(DataIntegrityViolationException.class, () -> followDbStorage.addFriend(1L, -1L), "Пользователь с id " + -1L + " не найден.");
    }

    @Test
    void testGetOneUserFriend() {
        userDbStorage.createUser(getUser());
        User user = getUser();
        user.setEmail("test1@mail.ru");
        user.setLogin("testLogin1");
        User savedUser = userDbStorage.createUser(user);
        followDbStorage.addFriend(1L, 2L);
        assertEquals(List.of(savedUser), followDbStorage.getAllFriends(1L));
    }

    @Test
    void testGetTwoUserFriends() {
        userDbStorage.createUser(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        User savedUser = userDbStorage.createUser(user1);
        User user2 = getUser();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("testLogin2");
        User savedUser2 = userDbStorage.createUser(user2);

        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(1L, 3L);
        assertEquals(List.of(savedUser, savedUser2), followDbStorage.getAllFriends(1L));
    }

    @Test
    void testGetEmptyFriendsOfFriend() {
        userDbStorage.createUser(getUser());
        User user = getUser();
        user.setEmail("test1@mail.ru");
        user.setLogin("testLogin1");
        userDbStorage.createUser(user);
        followDbStorage.addFriend(1L, 2L);
        assertEquals(new ArrayList<>(), followDbStorage.getAllFriends(2L));
    }

    @Test
    void testGetEmptyCommonFriends() {
        assertEquals(new ArrayList<>(), followDbStorage.getCommonFriends(1L, 2L));
    }

    @Test
    void testGetOneCommonFriend() {
        userDbStorage.createUser(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        userDbStorage.createUser(user1);
        User user2 = getUser();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("testLogin2");
        User savedUser2 = userDbStorage.createUser(user2);

        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(1L, 3L);
        followDbStorage.addFriend(2L, 3L);
        assertEquals(List.of(savedUser2), followDbStorage.getCommonFriends(1L, 2L));
    }

    @Test
    void testDeleteUserFriend() {
        userDbStorage.createUser(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        userDbStorage.createUser(user1);

        followDbStorage.addFriend(1L, 2L);
        assertDoesNotThrow(() -> followDbStorage.deleteFriend(1L, 2L));
    }

    @Test
    void testGetOneCommonFriendAfterDeletingOfFriend() {
        userDbStorage.createUser(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        userDbStorage.createUser(user1);
        User user2 = getUser();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("testLogin2");
        User savedUser2 = userDbStorage.createUser(user2);

        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(1L, 3L);
        followDbStorage.addFriend(2L, 3L);
        followDbStorage.deleteFriend(1L, 2L);
        assertEquals(List.of(savedUser2), followDbStorage.getCommonFriends(1L, 2L));
    }
}
