package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmService filmController;

    @BeforeEach
    public void tearDown() {
        filmController = new FilmService(new InMemoryFilmStorage());
    }

    @Test
    public void shouldCheckUpNormalValidation() {
        final Film film = filmController.create(
                        Film.builder()
                        .name("TestName")
                        .description("TestDescription")
                        .releaseDate(LocalDate.of(2022, 1, 1))
                        .duration(140)
                        .build()
        );
        final List<Film> films = filmController.findAll();

        assertNotNull(films, "List films is empty.");
        assertEquals(1, films.size(), "Wrong list size. Film has not been saved.");


        final Film film2 = filmController.update(
                Film.builder()
                        .id(film.getId())
                        .name("Name")
                        .description("Description")
                        .releaseDate(LocalDate.of(2022, 6, 1))
                        .duration(145)
                        .build()
        );

        final List<Film> films2 = filmController.findAll();

        assertNotNull(films2, "List users is empty.");
        assertEquals(1, films2.size(), "Wrong list size. User has not been saved.");

        final Film film3 = films2.get(0);
        assertEquals(film2.getDescription(), film3.getDescription(), "Descriptions don't match.");
        assertEquals(film2.getName(), film3.getName(), "Names don't match.");
        assertEquals(film2.getDuration(), film3.getDuration(), "Duration don't match.");
        assertEquals(film2.getReleaseDate(), film3.getReleaseDate(), "ReleaseDates don't match.");
    }


    @Test
    public void shouldCheckUpReleaseDateValidation() {
        assertThrows(ValidationException.class, () -> filmController.create(
                Film.builder()
                        .name("TestName")
                        .description("TestDescription")
                        .releaseDate(LocalDate.of(1657, 1, 1))
                        .duration(140)
                        .build()
        ), "December 28, 1895 is considered the birthday of cinema.");
    }

    @Test
    public void shouldCheckUpIdValidation() {
        final Film film = filmController.create(
                Film.builder()
                        .name("TestName")
                        .description("TestDescription")
                        .releaseDate(LocalDate.of(2022, 1, 1))
                        .duration(140)
                        .build()
        );
        final List<Film> films = filmController.findAll();

        assertNotNull(films, "List films is empty.");
        assertEquals(1, films.size(), "Wrong list size. Film has not been saved.");

        assertThrows(FilmNotFoundException.class, () -> filmController.update(
                Film.builder()
                        .id(5L)
                        .name("Name")
                        .description("Description")
                        .releaseDate(LocalDate.of(2020, 1, 1))
                        .duration(140)
                        .build()
        ), "Film with the same id doesn't exist.");

        assertThrows(FilmNotFoundException.class, () -> filmController.update(
                Film.builder()
                        .name("Name")
                        .description("Description")
                        .releaseDate(LocalDate.of(2020, 1, 1))
                        .duration(140)
                        .build()
        ), "Film id must not be empty.");
    }
}
