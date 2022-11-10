package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film findFilmById (Long filmId);

    void addLike (Long userId, Long filmId);

    void deleteLike (Long userId, Long filmId);

    List<Film> getTopFilms (Integer count);
}
