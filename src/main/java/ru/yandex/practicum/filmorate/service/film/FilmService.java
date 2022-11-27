package ru.yandex.practicum.filmorate.service.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.search.SearchSortBy;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmService {
    static LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);
    FilmStorage filmStorage;
    GenreService genreService;
    DirectorService directorService;

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public Film getById(Long filmId) {
        Film film = filmStorage.getById(filmId);
        genreService.loadGenres(List.of(film));
        directorService.loadDirectors(List.of(film));
        return film;
    }

    public Film create(Film film) {
        validator(film);
        Film filmWithId = filmStorage.create(film);
        if (film.getGenres() != null) {
            genreService.addGenresToFilm(filmWithId.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmDirectors(filmWithId.getId(), film.getDirectors());
        }
        return filmWithId;
    }

    public Film update(Film film) {
        validator(film);
        genreService.deleteFilmGenres(film.getId());
        directorService.deleteFilmDirectors(film.getId());
        if (film.getGenres() != null) {
            genreService.addGenresToFilm(film.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmDirectors(film.getId(), film.getDirectors());
        }
        return filmStorage.update(film);
    }

    public void deleteById(Long filmId) {
        filmStorage.deleteById(filmId);
    }

    public List<Film> getTop(Integer count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> films = filmStorage.getTop(count, genreId, year);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getCommon(Long userId, Long friendId) {
        List<Film> films = filmStorage.getCommon(userId, friendId);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getDirectorFilmsBy(Long directorId, DirectorSortBy sortBy) {
        directorService.getById(directorId);
        List<Film> films = filmStorage.getDirectorFilmsBy(directorId, sortBy);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> searchTopFilmsBy(String query, String by) {
        by = by.toLowerCase();
        boolean searchByFilmName = by.contains(SearchSortBy.title.name());
        boolean searchByDirectorName = by.contains(SearchSortBy.director.name());
        List<Film> films = filmStorage.searchTopFilmsBy(query, searchByFilmName, searchByDirectorName);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }
}


