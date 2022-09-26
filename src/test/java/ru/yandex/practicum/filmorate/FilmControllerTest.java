package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void tearDown() {
        filmController = new FilmController();
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
    public void shouldCheckUpFilmNameValidation() {
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

        assertThrows(ValidationFilmNameException.class, () -> filmController.create(
                Film.builder()
                        .name("TestName")
                        .description("TestDescription")
                        .releaseDate(LocalDate.of(2022, 1, 1))
                        .duration(140)
                        .build()
        ), "Film with the same name already exists.");

        assertThrows(ValidationFilmNameException.class, () -> filmController.create(
                Film.builder()
                        .name("")
                        .description("TestDescription")
                        .releaseDate(LocalDate.of(2022, 1, 1))
                        .duration(140)
                        .build()
        ), "Name must not be empty.");
    }

    @Test
    public void shouldCheckUpDescriptionValidation() {

        assertThrows(ValidationDescriptionException.class, () -> filmController.create(
                Film.builder()
                        .name("TestName")
                        .description("Have you ever heard of Hachiko the dog? No? Neither had I until we visited Tokyo." +
                                " If you ever go to Japan then odds are you will hear about him. Hachiko is a national " +
                                "hero to the Japanese! A dog so famous there have been several movies made about him.")
                        .releaseDate(LocalDate.of(2022, 1, 1))
                        .duration(140)
                        .build()
        ), "Description must be less then 200 symbols.");
    }

    @Test
    public void shouldCheckUpReleaseDateValidation() {
        assertThrows(ValidationReleaseDateException.class, () -> filmController.create(
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

        assertThrows(ValidationIdException.class, () -> filmController.update(
                Film.builder()
                        .id(5)
                        .name("Name")
                        .description("Description")
                        .releaseDate(LocalDate.of(2020, 1, 1))
                        .duration(140)
                        .build()
        ), "Film with the same id doesn't exist.");

        assertThrows(ValidationIdException.class, () -> filmController.update(
                Film.builder()
                        .name("Name")
                        .description("Description")
                        .releaseDate(LocalDate.of(2020, 1, 1))
                        .duration(140)
                        .build()
        ), "Film id must not be empty.");
    }

    @Test
    public void shouldCheckUpDurationValidation() {
        assertThrows(ValidationDurationException.class, () -> filmController.update(
                Film.builder()
                        .name("Name")
                        .description("Description")
                        .releaseDate(LocalDate.of(2020, 1, 1))
                        .duration(-55)
                        .build()
        ), "Duration must be positive number.");
    }

    @Test
    public void shouldCheckUpEmptyValidation() {
        assertThrows(ValidationException.class, () -> filmController.create(null), "Object Film is empty.");
    }




}
