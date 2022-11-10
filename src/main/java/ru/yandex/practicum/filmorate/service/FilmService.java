package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService (@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validator(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validatorId(film.getId());
        validator(film);
        return filmStorage.update(film);
    }

    public Film findFilmById (Long filmId) {
        validatorId(filmId);
        return filmStorage.findFilmById(filmId);
    }

    public void addLike (Long userId, Long filmId) {
        validatorId(filmId);
        filmStorage.addLike(userId, filmId);
    }

    public void deleteLike (Long userId, Long filmId) {
        validatorId(filmId);
        filmStorage.deleteLike(userId, filmId);
    }

    public List<Film> getTopFilms (Integer count) {
        return filmStorage.getTopFilms(count);
    }
    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            log.debug("releaseDate: " + film.getReleaseDate());
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }

    private void validatorId(Long id) {
        if (filmStorage.findFilmById(id) == null || id == null) {
            throw new FilmNotFoundException(String.format("Film with %d id not found", id));
        }
    }
}


