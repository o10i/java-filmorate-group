package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreDbTest {
    GenreDbStorage genreDbStorage;

    @Test
    void testFindGenreById() {
        assertEquals(Genre.builder().id(1L).name("Комедия").build(), genreDbStorage.findGenreById(1L));
    }

    @Test
    void testFindUnknownGenre() {
        assertThrows(DataNotFoundException.class, () -> genreDbStorage.findGenreById(-1L), "Genre с id -1 не найден.");
    }

    @Test
    void testFindAllGenres() {
        assertEquals(Genre.builder().id(1L).name("Комедия").build(), genreDbStorage.findAllGenres().get(0));
        assertEquals(6, genreDbStorage.findAllGenres().size());
    }
}