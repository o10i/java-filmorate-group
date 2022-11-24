package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.user.Follow;
import ru.yandex.practicum.filmorate.storage.follow.FollowDbStorage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FollowDbTest {
   /* private final JdbcTemplate jdbcTemplate;
    private final FollowDbStorage followDbStorage;

    private Follow getFollow() {
        return Follow.builder().userId(1L).friendId(2L).build();
    }

    private Follow getCommon() {
        return Follow.builder().userId(3L).friendId(2L).build();
    }
    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FOLLOW");
    }
    @Test
    public void testAddFriend() {
        Follow follow = getFollow();
        followDbStorage.addFriend(follow.getUserId(), follow.getFriendId());
        assertNotNull(followDbStorage.getAllFriends(follow.getUserId()), "Friends is empty");
    }

    @Test
    public void testDeleteFriend() {
        Follow follow = getFollow();
        followDbStorage.addFriend(follow.getUserId(), follow.getFriendId());
        followDbStorage.deleteFriend(follow.getUserId(), follow.getFriendId());
        assertNull(followDbStorage.getAllFriends(follow.getUserId()), "Friends is not empty after delete");
    }

    @Test
    public void testGetAllFriends() {
        Follow follow = getFollow();
        followDbStorage.addFriend(follow.getUserId(), follow.getFriendId());
        assertNotNull(followDbStorage.getAllFriends(1L), "Friends is empty");
    }

    @Test
    public void testGetCommonFriends() {
        Follow follow = getFollow();
        followDbStorage.addFriend(follow.getUserId(), follow.getFriendId());
        Follow common = getCommon();
        followDbStorage.addFriend(common.getUserId(), common.getFriendId());

        assertNotNull(followDbStorage.getCommonFriends(follow.getUserId(), common.getUserId())
                , "Common friends is empty");
    }*/
}
