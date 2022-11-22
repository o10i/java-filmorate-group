package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbTest {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User user = userStorage.create(User.builder()
                .email("myname@ya.ru")
                .login("login")
                .name("MyName")
                .birthday(LocalDate.of(2022, 1, 1))
                .build()
        );

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(us ->
                        assertThat(us).hasFieldOrPropertyWithValue("id", 1L)
                );

        assertThrows(UserNotFoundException.class, () -> userStorage.findUserById(-4L));
    }


}
