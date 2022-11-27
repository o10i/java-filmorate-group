package ru.yandex.practicum.filmorate.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Create;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Update;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@Validated(Create.class) @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable("userId") Long id) {
        userService.delete(id);
    }
}
