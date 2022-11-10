package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User create(User user);

    User update(User user);

    User findUserById(Long userId);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getAllFriends (Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);
}
