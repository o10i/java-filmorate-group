package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
