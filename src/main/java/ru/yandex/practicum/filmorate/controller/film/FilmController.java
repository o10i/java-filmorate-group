package ru.yandex.practicum.filmorate.controller.film;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;


@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms (@RequestParam(defaultValue = "10") @Positive Integer count,
                                   @RequestParam Optional<Integer> genreId,
                                   @RequestParam Optional<Integer> year) {
        return filmService.getTopFilms(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedDirectorFilms(@PathVariable("directorId") Long directorId,
                                             @RequestParam String sortBy) {
        return filmService.getSortedDirectorFilms(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam(value = "userId") Long userId,
                                     @RequestParam(value = "friendId") Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<Film> getTopSortedSearchedFilms(@RequestParam String query,
                                                @RequestParam String by) {
        return filmService.getTopSortedSearchedFilms(query, by);
    }

}
