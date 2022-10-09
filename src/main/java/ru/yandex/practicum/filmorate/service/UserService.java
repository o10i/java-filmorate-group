package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService (UserStorage userStorage) {
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

        findUserById(userId).getFriends().add(friendId);
        findUserById(friendId).getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);

        findUserById(userId).getFriends().remove(friendId);
        findUserById(friendId).getFriends().remove(userId);
    }

    public List<User> getAllFriends (Long userId) {
        validatorId(userId);
        return findUserById(userId).getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        validatorId(userId);
        validatorId(friendId);

        return findUserById(userId).getFriends().stream()
                .filter(id -> findUserById(friendId).getFriends().contains(id))
                .map(this::findUserById)
                .collect(Collectors.toList());
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
