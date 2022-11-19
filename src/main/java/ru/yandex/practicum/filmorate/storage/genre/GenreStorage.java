package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {

    List<Genre> findAllGenre();

    Genre findGenreById(Long genreId);

    void addFilmsGenre (Long filmId, LinkedHashSet<Genre> genres);

    void loadGenres(List<Film> films);
    void deleteFilmsGenre(Long filmId);

}
