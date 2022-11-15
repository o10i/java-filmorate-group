package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FollowDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class FollowService {

    private final FollowDbStorage followDbStorage;

    private final UserService userService;

    @Autowired
    public FollowService(FollowDbStorage followDbStorage, UserService userService) {
        this.followDbStorage = followDbStorage;
        this.userService = userService;
    }

    public void addFriend(Long userId, Long friendId) {
        userService.validatorId(userId);
        userService.validatorId(friendId);
        followDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userService.validatorId(userId);
        userService.validatorId(friendId);
        followDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends (Long userId) {
        userService.validatorId(userId);
        return followDbStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        userService.validatorId(userId);
        userService.validatorId(friendId);
        return followDbStorage.getCommonFriends(userId, friendId);
    }
}
