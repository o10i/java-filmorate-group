package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> findAll(){
        return userStorage.findAll();
    }

    public User create(User user) {
        validator(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validator(user);
        findUserById(user.getId());
        return userStorage.update(user);
    }

    public User findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }

    private void validator (User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Your login must not contains space symbols.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
