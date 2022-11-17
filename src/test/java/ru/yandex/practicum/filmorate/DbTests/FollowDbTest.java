package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FollowDbStorage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FollowDbTest {

    private final FollowDbStorage followDbStorage;


    @Test
    public void testAddFriend() {
        followDbStorage.addFriend(2L, 4L);
    }

    @Test
    public void testDeleteFriend() {
        followDbStorage.deleteFriend(2L, 4L);
    }

    @Test
    public void testGetAllFriends() {
        followDbStorage.addFriend(1L, 2L);

        assertNotNull(followDbStorage.getAllFriends(1L), "Friends is empty");
    }

    @Test
    public void testGetCommonFriends() {
        followDbStorage.addFriend(1L, 5L);
        followDbStorage.addFriend(2L, 5L);

        assertNotNull(followDbStorage.getCommonFriends(1L, 2L), "Common friends is empty");

    }
}
