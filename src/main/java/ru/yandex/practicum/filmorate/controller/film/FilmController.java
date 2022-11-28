package ru.yandex.practicum.filmorate.controller.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.film.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;


@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmController {

    FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable("id") Long filmId) {
        return filmService.getById(filmId);
    }

    @PostMapping
    public Film create(@Validated(Marker.OnCreate.class) @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Validated(Marker.OnUpdate.class) @RequestBody Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("{filmId}")
    public void deleteById(@PathVariable("filmId") Long filmId) {
        filmService.deleteById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTop(@RequestParam(defaultValue = "10") @Positive Integer count,
                             @RequestParam(required = false) Optional<Integer> genreId,
                             @RequestParam(required = false) Optional<Integer> year) {
        return filmService.getTop(count, genreId, year);
    }

    @GetMapping("/common")
    public List<Film> getCommon(@RequestParam(value = "userId") Long userId,
                                @RequestParam(value = "friendId") Long friendId) {
        return filmService.getCommon(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilmsBy(@PathVariable("directorId") Long directorId,
                                         @RequestParam DirectorSortBy sortBy) {
        return filmService.getDirectorFilmsBy(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchTopFilmsBy(@RequestParam String query,
                                       @RequestParam String by) {
        return filmService.searchTopFilmsBy(query, by);
    }
}
