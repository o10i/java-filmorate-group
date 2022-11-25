package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDbTest {
    JdbcTemplate jdbcTemplate;
    UserDbStorage userDbStorage;

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
    void testSaveUser() {
        User user = getUser();
        User userSaved = userDbStorage.createUser(user);
        user.setId(1L);
        assertEquals(user, userSaved);
    }

    @Test
    void testSaveUserWithLoginSpace() {
        User user = getUser();
        user.setLogin("test test");
        user.setLogin("test test");
        assertThrows(DataIntegrityViolationException.class, () -> userDbStorage.createUser(user), "Логин должен быть без пробелов.");
    }

    @Test
    void testSaveUserWithEmptyLogin() {
        User user = getUser();
        user.setLogin("");
        assertThrows(DataIntegrityViolationException.class, () -> userDbStorage.createUser(user), "Логин не должен быть пустым.");
    }

    @Test
    void testSaveUserWithFutureBirthday() {
        User user = getUser();
        user.setBirthday(Date.valueOf("2122-11-03").toLocalDate());
        assertThrows(DataIntegrityViolationException.class, () -> userDbStorage.createUser(user), "День рождения не может быть в будущем.");
    }

    @Test
    void testSaveUserWithEmptyEmail() {
        User user = getUser();
        user.setEmail("");
        assertThrows(DataIntegrityViolationException.class, () -> userDbStorage.createUser(user), "Электронная почта не может быть пустой.");
    }

    @Test
    void testSaveUserWithoutAtInEmail() {
        User user = getUser();
        user.setEmail("testmail.ru");
        assertThrows(DataIntegrityViolationException.class, () -> userDbStorage.createUser(user), "Электронная почта должна содержать символ '@'.");
    }

    @Test
    void testUpdateUser() {
        User user = userDbStorage.createUser(getUser());
        user.setLogin("testUpdateLogin");
        assertEquals(user, userDbStorage.updateUser(user));
    }

    @Test
    void testFindAllUsers() {
        User user = userDbStorage.createUser(getUser());
        List<User> users = List.of(user);
        assertEquals(users, userDbStorage.findAllUsers());
    }

    @Test
    void testFindUserById() {
        User user = userDbStorage.createUser(getUser());
        user.setId(1L);
        assertEquals(user, userDbStorage.findUserById(1L));
    }

    @Test
    void testFindUnknownUser() {
        assertThrows(UserNotFoundException.class, () -> userDbStorage.findUserById(9999L), "Пользователь с id " + 9999 + " не найден.");
    }
}
