package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;
import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User create(User user);

    User updateUser(User user);

    User findUserById(Long userId);
    void deleteById(Long userId);
}
