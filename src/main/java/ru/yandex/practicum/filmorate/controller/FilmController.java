package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.model.Film.FILM_BIRTH;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int baseId = 0;
    private final Map<Integer, Film> films = new HashMap<>(); // Id - Film

    @GetMapping
    public List<Film> findAll() {
        log.debug("Count of films: " + films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validator(film);
        film.setId(++baseId);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film)  {
        validator(film);
        if (film.getId() == null) {
            throw new ValidationException("Film id must not be empty.");
        }
        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Id: " + film.getId());
            throw new ValidationException("Film with the same id doesn't exist.");
        }
        return film;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            log.debug("releaseDate: " + film.getReleaseDate());
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }

}
