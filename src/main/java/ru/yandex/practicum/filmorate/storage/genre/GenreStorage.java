package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {

    List<Genre> findAllGenres();

    Genre findGenreById(Long genreId);

    void addGenresToFilm(Long filmId, LinkedHashSet<Genre> genres);

    void loadGenres(List<Film> films);
    void deleteFilmGenres(Long filmId);

}
