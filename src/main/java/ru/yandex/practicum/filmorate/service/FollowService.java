package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.follow.FollowStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowStorage followStorage;
    private final UserService userService;
    private final FeedService feedService;

    public void addFriend(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);
        followStorage.addFriend(userId, friendId);
        feedService.saveEvent(Event.createEvent(userId, EventType.FRIEND, Operation.ADD, friendId));
    }

    public void deleteFriend(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);
        followStorage.deleteFriend(userId, friendId);
        feedService.saveEvent(Event.createEvent(userId, EventType.FRIEND, Operation.REMOVE, friendId));
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
