package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public void addLike (Long userId, Long filmId) {
        findFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike (Long userId, Long filmId) {
        if(!findFilmById(filmId).getLikes().contains(userId)) {
            throw new UserNotFoundException(String.format("User with %d id not found", userId));
        }
        findFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getTopFilms (Integer count) {
        return findAll().stream()
                .sorted((f0, f1) -> Integer.compare(f1.getLikes().size(), f0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
