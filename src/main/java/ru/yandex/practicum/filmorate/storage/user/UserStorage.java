package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User findUserById(Long userId);

    List<Film> getRecommendations(Long userId);
}
