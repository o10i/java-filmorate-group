package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    private final GenreService genreService;

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        genreService.loadGenres(films);
        return films;
    }

    public Film create(Film film) {
        validator(film);
        Film filmWithId = filmStorage.create(film);
        if (film.getGenres() != null) {
            genreService.addFilmsGenre(filmWithId.getId(), film.getGenres());
        }
        return filmWithId;
    }

    public Film update(Film film) {
        validator(film);
        filmStorage.findFilmById(film.getId());
        if (film.getGenres() != null) {
            genreService.deleteFilmsGenre(film.getId());
            genreService.addFilmsGenre(film.getId(), film.getGenres());
        }
        return filmStorage.update(film);
    }

    public Film findFilmById (Long filmId) {
        Film film = filmStorage.findFilmById(filmId);
        genreService.loadGenres(List.of(film));
        return film;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> films = filmStorage.getTopFilms(count);
        genreService.loadGenres(films);
        return films;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }
}


