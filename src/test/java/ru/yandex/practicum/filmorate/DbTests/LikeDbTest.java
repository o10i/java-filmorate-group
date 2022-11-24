package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbTest {

    private final LikeDbStorage likeDbStorage;

   /* @Test
    public void testAddLike() {
        likeDbStorage.addLike(1L,1L);
        likeDbStorage.addLike(2L,1L);
    }

    @Test
    public void testDeleteLike() {
        likeDbStorage.deleteLike(2L,1L);
    }

    @Test
    public void testGetLikes() {
        assertNotNull(likeDbStorage.getLikes(1L,1L));
    }

}
