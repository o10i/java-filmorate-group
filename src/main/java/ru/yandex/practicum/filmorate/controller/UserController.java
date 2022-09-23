package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int baseId = 0;
    private final Map<Integer, User> users = new HashMap<>(); // Id - User

    @GetMapping
    public List<User> findAll() {
        log.debug("Count of users: " + users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        validator(user);
        users.values().forEach((us)->{
            if (user.getEmail().equals(us.getEmail())) {
                log.debug("email: " + user.getEmail());
                throw new ValidationException("User with the same email address already exists.");
            }
        });

        users.values().forEach((us)->{
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

    @PutMapping
    public User exchange(@RequestBody User user) throws ValidationException {
        validator(user);
        if (user.getId() == null) {
            throw new ValidationException("Your id must not be empty.");
        }
        users.values().forEach((us)->{
            if (user.getId() == us.getId()) {
                users.put(user.getId(), user);
            } else {
                log.debug("Id: " + user.getId());
                throw new ValidationException("User with the same id doesn't exist.");
            }
        });
        return user;
    }

    private void validator(User user) throws ValidationException {
        if(user.getEmail().isBlank() || !user.getEmail().contains("@") || user.getEmail() == null) {
            log.debug("email: " + user.getEmail());
            throw new ValidationException("Your email address must not be empty or must contains 'at' symbol. Try again.");
        }
        if(user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.debug("login: " + user.getLogin());
            throw new ValidationException("Your login must not be empty or contains space symbols. Try again.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("birthday: " + user.getBirthday());
            throw new ValidationException("Birthday must no to be in the future ;)");
        }
    }
}
