package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService (@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll(){
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        validatorId(user.getId());
        return userStorage.update(user);
    }

    public User findUserById(Long userId) {
        validatorId(userId);
        return userStorage.findUserById(userId);
    }

    public void validatorId(Long id) {
        if (userStorage.findUserById(id) == null || id == null) {
            throw new UserNotFoundException(String.format("User with %d id not found", id));
        }
    }
}
