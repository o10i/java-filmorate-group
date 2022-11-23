package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.follow.FollowStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowStorage followStorage;

    private final UserService userService;


    public void addFriend(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);
        followStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);
        followStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends (Long userId) {
        userService.findUserById(userId);
        return followStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);
        return followStorage.getCommonFriends(userId, friendId);
    }
}
