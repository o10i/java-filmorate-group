package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.SortType;
import ru.yandex.practicum.filmorate.model.search.SearchType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final DirectorService directorService;

    public List<Film> findAllFilms() {
        List<Film> films = filmStorage.findAllFilms();
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public Film createFilm(Film film) {
        validator(film);
        Film filmWithId = filmStorage.createFilm(film);
        if (film.getGenres() != null) {
            genreService.addGenresToFilm(filmWithId.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmsDirector(filmWithId.getId(), film.getDirectors());
        }
        return filmWithId;
    }

    public Film updateFilm(Film film) {
        validator(film);
        filmStorage.findFilmById(film.getId());
        if (film.getGenres() != null) {
            genreService.deleteFilmGenres(film.getId());
            genreService.addGenresToFilm(film.getId(), film.getGenres());
        } else {
            genreService.deleteFilmGenres(film.getId());
        }
        if (film.getDirectors() != null) {
            directorService.deleteFilmsDirector(film.getId());
            directorService.addFilmsDirector(film.getId(), film.getDirectors());
        } else {
            directorService.deleteFilmsDirector(film.getId());
        }
        return filmStorage.updateFilm(film);
    }

    public Film findFilmById(Long filmId) {
        Film film = filmStorage.findFilmById(filmId);
        genreService.loadGenres(List.of(film));
        directorService.loadDirectors(List.of(film));
        return film;
    }

    public List<Film> getTopFilms(Integer count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> films = filmStorage.getTopFilms(count, genreId, year);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);
        genreService.loadGenres(films);
        return films;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }

    public List<Film> getSortedDirectorFilms(Long directorId, String sortBy) {
        try {
            SortType.valueOf(sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректный тип сортировки");
        }
        directorService.findDirectorById(directorId);
        List<Film> films = filmStorage.getSortedDirectorFilms(directorId, sortBy);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getTopSortedSearchedFilms(String query, String by) {
        List<Film> films = filmStorage.getTopFilms(-1, Optional.empty(), Optional.empty());
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        by = by.toUpperCase();
        boolean searchByFilmName = by.contains(SearchType.TITLE.name());
        boolean searchByDirectorName = by.contains(SearchType.DIRECTOR.name());
        if (searchByFilmName || searchByDirectorName) {
            return films.stream()
                    .filter(film -> (searchByDirectorName && film.getDirectors().stream()
                            .anyMatch(director -> director.getName().toLowerCase().contains(query.toLowerCase())))
                            || (searchByFilmName && film.getName().toLowerCase().contains(query.toLowerCase())))
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Некорректный тип поиска");
    }
}


