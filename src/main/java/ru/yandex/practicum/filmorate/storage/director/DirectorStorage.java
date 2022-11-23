package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.LinkedHashSet;
import java.util.List;

public interface DirectorStorage {

    List<Director> findAllDirectors();

    Director findDirectorById (Long id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirectorById(Long id);

    void deleteFilmsDirector(Long filmId);

    void addFilmsDirector(Long filmId, LinkedHashSet<Director> directors);

    void loadDirectors(List<Film> films);
}
