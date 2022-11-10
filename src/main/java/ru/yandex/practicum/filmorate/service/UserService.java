package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
@Slf4j
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
        validator(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validator(user);
        validatorId(user.getId());
        return userStorage.update(user);
    }

    public User findUserById(Long userId) {
        validatorId(userId);
        return userStorage.findUserById(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);
        userStorage.addFriend(userId, friendId);

    }

    public void deleteFriend(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends (Long userId) {
        validatorId(userId);
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);
        return userStorage.getCommonFriends(userId, friendId);
    }

    private void validator(User user) {
        if(user.getLogin().contains(" ")) {
            log.debug("login: " + user.getLogin());
            throw new ValidationException("Your login must not contains space symbols. Try again.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validatorId(Long id) {
        if (userStorage.findUserById(id) == null || id == null) {
            throw new UserNotFoundException(String.format("User with %d id not found", id));
        }
    }
}
