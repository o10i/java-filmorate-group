package ru.yandex.practicum.filmorate.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {
    UserStorage userStorage;

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        validator(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validator(user);
        findUserById(user.getId());
        return userStorage.updateUser(user);
    }

    public User findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }

    public void deleteUserById(Long userId) {
        userStorage.findUserById(userId);
        userStorage.deleteUserById(userId);
    }

    private void validator(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Your login must not contains space symbols.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
