package ru.yandex.practicum.filmorate.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.storage.follow.FollowStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowService {
    FollowStorage followStorage;
    UserService userService;
    FeedService feedService;

    public void addFriend(Long userId, Long friendId) {
        userService.getById(userId);
        userService.getById(friendId);
        followStorage.addFriend(userId, friendId);
        feedService.saveEvent(Event.createEvent(userId, EventType.FRIEND, Operation.ADD, friendId));
    }

    public void deleteFriend(Long userId, Long friendId) {
        followStorage.deleteFriend(userId, friendId);
        feedService.saveEvent(Event.createEvent(userId, EventType.FRIEND, Operation.REMOVE, friendId));
    }

    public List<User> getAllFriends(Long userId) {
        userService.getById(userId);
        return followStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        userService.getById(userId);
        userService.getById(friendId);
        return followStorage.getCommonFriends(userId, friendId);
    }
}
