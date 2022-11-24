package DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbTest {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;

    private Film getFilm() {
        return Film.builder().name("testName").releaseDate(Date.valueOf("1979-04-17").toLocalDate()).description("testDescription").duration(100).rate(4).mpa(Mpa.builder().id(1L).name("G").build()).genres(new LinkedHashSet<>()).directors(new LinkedHashSet<>()).build();
    }

    private User getUser() {
        return User.builder().email("test@mail.ru").login("testLogin").name("testName").birthday(Date.valueOf("1946-08-20").toLocalDate()).build();
    }

    private Genre getGenre() {
        return Genre.builder().id(2L).name("Драма").build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM LIKES");
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM MOVIE");
        jdbcTemplate.update("DELETE FROM DIRECTORS");
        jdbcTemplate.update("ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE MOVIE ALTER COLUMN ID RESTART WITH 1");

    }

    @Test
    void deleteFilm() {
        Film film = getFilm();
        filmDbStorage.create(film);
        filmDbStorage.deleteFilmById(1L);
        assertThrows(FilmNotFoundException.class, () -> filmDbStorage.findFilmById(1L));
    }

    @Test
    void deleteWrongFilm() {
        Film film = getFilm();
        filmDbStorage.create(film);
        assertThrows(FilmNotFoundException.class, () -> filmDbStorage.deleteFilmById(444L));
    }

    @Test
    void deleteLikesAfterDeletingFilm() {
        filmDbStorage.create(getFilm());
        userDbStorage.create(getUser());
        likeDbStorage.addLike(1L, 1L);
        assertEquals(1, likeDbStorage.getLikes(1l, 1l).size());
        filmDbStorage.deleteFilmById(1L);
        assertEquals(0, likeDbStorage.getLikes(1l, 1l).size());
    }
}