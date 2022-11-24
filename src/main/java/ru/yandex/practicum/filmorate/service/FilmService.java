package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.SortType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final DirectorService directorService;

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public Film create(Film film) {
        validator(film);
        Film filmWithId = filmStorage.create(film);
        if (film.getGenres() != null) {
            genreService.addFilmsGenre(filmWithId.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmsDirector(filmWithId.getId(), film.getDirectors());
        }
        return filmWithId;
    }

    public Film update(Film film) {
        validator(film);
        filmStorage.findFilmById(film.getId());
        if (film.getGenres() != null) {
            genreService.deleteFilmsGenre(film.getId());
            genreService.addFilmsGenre(film.getId(), film.getGenres());
        } else {
            genreService.deleteFilmsGenre(film.getId());
        }
        if (film.getDirectors() != null) {
            directorService.deleteFilmsDirector(film.getId());
            directorService.addFilmsDirector(film.getId(), film.getDirectors());
        } else {
            directorService.deleteFilmsDirector(film.getId());
        }
        return filmStorage.update(film);
    }

    public Film findFilmById(Long filmId) {
        Film film = filmStorage.findFilmById(filmId);
        genreService.loadGenres(List.of(film));
        directorService.loadDirectors(List.of(film));
        return film;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> films = filmStorage.getTopFilms(count);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getCommonFilms (Long userId, Long friendId) {
        List<Film> films = filmStorage.getCommonFilms(userId,friendId);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }

    public List<Film> getSortedDirectorFilms(Long directorId, String sortBy) {
        SortType sortType;
        try {
            sortType = SortType.valueOf(sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректный тип сортировки");
        }
        directorService.findDirectorById(directorId);
        List<Film> films;
        if (sortType == SortType.YEAR) {
            films = filmStorage.getDirectorFilmsSortedByYear(directorId);
        } else {
            films = filmStorage.getDirectorFilmsSortedByLikes(directorId);
        }
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public void deleteFilmById(Long filmId) {
        filmStorage.deleteFilmById(filmId);
    }
}


