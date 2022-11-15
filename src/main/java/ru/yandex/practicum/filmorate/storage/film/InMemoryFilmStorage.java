package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private long baseId = 0;
    private final Map<Long, Film> films = new HashMap<>(); // Id - Film


    public List<Film> findAll() {
        log.debug("Count of films: " + films.size());
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        film.setId(++baseId);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film)  {
        films.put(film.getId(), film);
        return film;
    }

    public Film findFilmById (Long filmId) {
        return films.get(filmId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return null;
    }
}
