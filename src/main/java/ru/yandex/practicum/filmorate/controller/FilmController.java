package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        films.values().forEach((fl)->{
            if (film.getName().equals(fl.getName())) {
                log.debug("name: " + film.getName());
                throw new ValidationFilmNameException("Film with the same name already exists.");
            }
        });
        film.setId(++baseId);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film)  {
        validator(film);
        if (film.getId() == null) {
            throw new ValidationIdException("Film id must not be empty.");
        }
        films.values().forEach((fl)->{
            if (film.getId() == fl.getId()) {
                films.put(film.getId(), film);
            } else {
                log.debug("Id: " + film.getId());
                throw new ValidationIdException("Film with the same id doesn't exist.");
            }
        });
        return film;
    }

    private void validator(Film film) {
        if(film == null) {
            throw new ValidationException("Object Film is empty.");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("name: " + film.getName());
            throw new ValidationFilmNameException("Name must not be empty.");
        }
        if (film.getDescription().length() > 200) {
            log.debug("description length: " + film.getDescription().length());
            throw new ValidationDescriptionException("Description must be less then 200 symbols.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("releaseDate: " + film.getReleaseDate());
            throw new ValidationReleaseDateException("December 28, 1895 is considered the birthday of cinema.");
        }
        if (film.getDuration() <= 0) {
            log.debug("duration: " + film.getDuration());
            throw new ValidationDurationException("Duration must be positive number.");
        }
    }

}
