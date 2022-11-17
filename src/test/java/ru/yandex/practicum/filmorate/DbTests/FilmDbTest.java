package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbTest {
    private final FilmDbStorage filmDbStorage;


    @Test
    public void testFindFilmById() {
        Film film = filmDbStorage.create(Film.builder()
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(5)
                .mpa(Mpa.builder()
                        .id(2L)
                        .build())
                .build()
        );

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.findFilmById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 1L)
                );

        assertThrows(FilmNotFoundException.class, () -> filmDbStorage.findFilmById(-4L));
    }

    @Test
    public void testUpdate() {
        Film film = filmDbStorage.update(Film.builder()
                .id(1L)
                .name("TestNa")
                .description("TestDescript")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(10)
                .rate(5)
                .mpa(Mpa.builder()
                        .id(2L)
                        .build())
                .build()
        );
    }

    @Test
    public void testTop() {
        assertTrue(filmDbStorage.getTopFilms(5).isEmpty(), "Films is not empty");
        filmDbStorage.create(Film.builder()
                .name("Test")
                .description("TestDe")
                .releaseDate(LocalDate.of(2022, 2, 1))
                .duration(107)
                .rate(4)
                .mpa(Mpa.builder()
                        .id(2L)
                        .build())
                .build()
        );
        filmDbStorage.create(Film.builder()
                .name("T")
                .description("Te")
                .releaseDate(LocalDate.of(2022, 2, 1))
                .duration(109)
                .rate(4)
                .mpa(Mpa.builder()
                        .id(2L)
                        .build())
                .build()
        );

        assertNotNull(filmDbStorage.getTopFilms(2), "Films is empty");
        assertEquals(1, filmDbStorage.getTopFilms(1).size(), "Wrong list size. Film has not been saved.");
    }

    @Test
    public void testFindAll() {
        assertNotNull(filmDbStorage.findAll(), "Films is empty");
        assertEquals(2, filmDbStorage.findAll().size(), "Wrong list size. Film has not been saved.");
    }
}
