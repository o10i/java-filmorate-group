package ru.yandex.practicum.filmorate.DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void testFindMpaById() {
        assertEquals(Mpa.builder().id(1L).name("G").build(), mpaDbStorage.findMPAById(1L));
    }

    @Test
    void testFindUnknownMpa() {
        assertThrows(DataNotFoundException.class, () -> mpaDbStorage.findMPAById(-1L), "Mpa с id -1 не найден.");
    }

    @Test
    void testFindAllMpa() {
        assertEquals(Mpa.builder().id(1L).name("G").build(), mpaDbStorage.findAllMPA().get(0));
        assertEquals(5, mpaDbStorage.findAllMPA().size());
    }
}
