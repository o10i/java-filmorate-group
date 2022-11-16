package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {

    private static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    private final GenreService genreService;

    @Autowired
    public FilmService (@Qualifier("filmDbStorage") FilmStorage filmStorage, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
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
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }
}


