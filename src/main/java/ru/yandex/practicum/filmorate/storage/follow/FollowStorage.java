package ru.yandex.practicum.filmorate.storage.follow;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface FollowStorage {


    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);
}
