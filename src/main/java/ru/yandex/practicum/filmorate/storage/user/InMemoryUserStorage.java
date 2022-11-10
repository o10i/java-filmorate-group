package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private long baseId = 0;
    private final Map<Long, User> users = new HashMap<>(); // Id - User

    public List<User> findAll() {
        log.debug("Count of users: " + users.size());
        return new ArrayList<>(users.values());
    }


    public User create(User user) {
        users.values().forEach((us)->{
            if (user.getEmail().equals(us.getEmail())) {
                log.debug("email: " + user.getEmail());
                throw new ValidationException("User with the same email address already exists.");
            }
            if (user.getLogin().equals(us.getLogin())) {
                log.debug("login: " + user.getLogin());
                throw new ValidationException("User with the same login already exists.");
            }
        });

        user.setId(++baseId);
        log.debug("User to save: " + user.toString());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User findUserById(Long userId) {
        return users.get(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        findUserById(userId).getFriends().add(friendId);
        findUserById(friendId).getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        findUserById(userId).getFriends().remove(friendId);
        findUserById(friendId).getFriends().remove(userId);
    }

    public List<User> getAllFriends (Long userId) {
        return findUserById(userId).getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        return findUserById(userId).getFriends().stream()
                .filter(id -> findUserById(friendId).getFriends().contains(id))
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

}
