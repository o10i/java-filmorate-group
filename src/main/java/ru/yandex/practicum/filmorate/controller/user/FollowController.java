package ru.yandex.practicum.filmorate.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.FollowService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/friends")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowController {
    FollowService followService;

    @PutMapping("/{friendId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        followService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        followService.deleteFriend(userId, friendId);
    }

    @GetMapping
    public List<User> getFriends(@PathVariable("id") Long userId) {
        return followService.getAllFriends(userId);
    }

    @GetMapping("/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long friendId) {
        return followService.getCommonFriends(userId, friendId);
    }
}
