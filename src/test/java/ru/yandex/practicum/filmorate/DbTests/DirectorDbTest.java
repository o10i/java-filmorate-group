package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorDbTest {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorDbStorage directorDbStorage;

    private Director getDirector() {
        return Director.builder().name("testName").build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM DIRECTORS");
        jdbcTemplate.update("ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void testCreateDirector() {
        Director director = getDirector();
        Director savedDirector = directorDbStorage.createDirector(director);
        director.setId(1L);
        assertEquals(director, savedDirector);
    }

    @Test
    void testCreateDirectorWithEmptyName() {
        Director director = getDirector();
        director.setName("");
        assertThrows(DataIntegrityViolationException.class, () -> directorDbStorage.createDirector(director), "Имя не должно быть пустым.");
    }

    @Test
    void testUpdateDirector() {
        Director director = directorDbStorage.createDirector(getDirector());
        director.setName("testUpdateName");
        assertEquals(director, directorDbStorage.updateDirector(director));
    }

    @Test
    void testUpdateUnknownDirector() {
        Director director = getDirector();
        director.setId(9999L);
        assertThrows(DataNotFoundException.class, () -> directorDbStorage.updateDirector(director), "Режиссёр не найден.");
    }

    @Test
    void testFindAllDirectors() {
        Director director = directorDbStorage.createDirector(getDirector());
        List<Director> directors = List.of(director);
        assertEquals(directors, directorDbStorage.findAllDirectors());
    }

    @Test
    void testFindDirectorById() {
        Director director = directorDbStorage.createDirector(getDirector());
        Director director1 = directorDbStorage.createDirector(getDirector());
        Director director2 = directorDbStorage.createDirector(getDirector());
        assertEquals(director, directorDbStorage.findDirectorById(1L));
        assertEquals(director1, directorDbStorage.findDirectorById(2L));
        assertEquals(director2, directorDbStorage.findDirectorById(3L));
    }

    @Test
    void testFindUnknownDirector() {
        assertThrows(DataNotFoundException.class, () -> directorDbStorage.findDirectorById(9999L), "Режиссёр с id " + 9999 + " не найден.");
    }
}
