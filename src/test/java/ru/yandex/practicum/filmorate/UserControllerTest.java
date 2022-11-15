package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    /*private UserService userController;

    @BeforeEach
    void tearDown() {
        userController = new UserService(new InMemoryUserStorage());
    }

    @Test
    public void shouldCheckUpNormalValidation() {
        final User user = userController.create(
                User.builder()
                .login("Test")
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2022, 1, 1))
                .build()
        );
        final List<User> users = userController.findAll();

        assertNotNull(users, "List users is empty.");
        assertEquals(1, users.size(), "Wrong list size. User has not been saved.");
        assertEquals(1, user.getId(), "Wrong user id.");

        final User user2 = userController.update(
                        User.builder()
                         .id(user.getId())
                         .login("TestLogin")
                         .name("TestName")
                         .email("mail@yandex.ru")
                         .birthday(LocalDate.of(2022, 6, 1))
                         .build()
        );

        final List<User> users2 = userController.findAll();

        assertNotNull(users2, "List users is empty.");
        assertEquals(1, users2.size(), "Wrong list size. User has not been saved.");
        assertEquals(1, user2.getId(), "Wrong user id.");

        final User user3 = users2.get(0);
        assertEquals(user2.getLogin(), user3.getLogin(), "Logins don't match.");
        assertEquals(user2.getName(), user3.getName(), "Names don't match.");
        assertEquals(user2.getEmail(), user3.getEmail(), "Emails don't match.");
        assertEquals(user2.getBirthday(), user3.getBirthday(), "Birthdays don't match.");
    }

    @Test
    public void shouldCheckUpEmailValidation() {
        final User user = userController.create(
                User.builder()
                .login("Test")
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2022, 1, 1))
                .build()
        );
        final List<User> users = userController.findAll();

        assertNotNull(users, "List users is empty. User has not been saved.");
        assertEquals(1, users.size(), "Wrong list size. User has not been saved.");

        assertThrows(ValidationException.class, () -> userController.create(
                        User.builder()
                        .login("TestLogin")
                        .name("name")
                        .email("mail@mail.ru")
                        .birthday(LocalDate.of(2022, 1, 1))
                        .build()
                ), "User with the same email address already exists.");

    }

    @Test
    public void shouldCheckUpLoginValidation() {
        final User user = userController.create(
                 User.builder()
                .login("Test")
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2022, 1, 1))
                .build()
        );
        final List<User> users = userController.findAll();

        assertNotNull(users, "List users is empty. User has not been saved.");
        assertEquals(1, users.size(), "Wrong list size. User has not been saved.");

        assertThrows(ValidationException.class, () -> userController.create(
                        User.builder()
                        .login("Test")
                        .name("name")
                        .email("mail@yandex.ru")
                        .birthday(LocalDate.of(2022, 1, 1))
                        .build()
                ), "User with the same login already exists.");

        assertThrows(ValidationException.class, () -> userController.create(
                        User.builder()
                        .login("Test login")
                        .name("name")
                        .email("mail@yandex.ru")
                        .birthday(LocalDate.of(2022, 1, 1))
                        .build()
                ), "Your login must not contains space symbols.");
    }

    @Test
    public void shouldCheckUpNameValidation() {
        final User user = userController.create(
                User.builder()
                .login("Test")
                .name("")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(2022, 1, 1))
                .build()
        );
        assertEquals(user.getLogin(), user.getName(), "Name and login don't match.");
    }

    @Test
    public void shouldCheckUpIdValidation() {
        final User user = userController.create(
                User.builder()
                .login("Test")
                .name("name")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(2022, 1, 1))
                .build());
        final List<User> users = userController.findAll();

        assertNotNull(users, "List users is empty. User has not been saved.");
        assertEquals(1, users.size(), "Wrong list size. User has not been saved.");

        assertThrows(UserNotFoundException.class, () -> userController.update(
                        User.builder()
                        .id(5L)
                        .login("TestLogin")
                        .name("name")
                        .email("mail@mail.ru")
                        .birthday(LocalDate.of(2022, 1, 1))
                        .build()
                ), "User with the same id doesn't exist.");

        assertThrows(UserNotFoundException.class, () -> userController.update(
                                User.builder()
                                .login("testLogin")
                                .name("testName")
                                .email("test@yandex.ru")
                                .birthday(LocalDate.of(2022, 1, 1))
                                .build()
                ), "Your id must not be empty.");
    }*/
}
