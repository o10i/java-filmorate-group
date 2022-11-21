package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> findAll() {
        return directorStorage.findAll();
    }

    public Director findDirectorById(Long id) {
        return directorStorage.findDirectorById(id);
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        findDirectorById(director.getId());
        return directorStorage.update(director);
    }

    public void deleteDirectorById(@PathVariable("id") Long id) {
        directorStorage.deleteDirectorById(id);
    }

    public void deleteFilmsDirector(Long filmId){
        directorStorage.deleteFilmsDirector(filmId);
    }

    public void addFilmsDirector(Long filmId, LinkedHashSet<Director> directors) {
        directorStorage.addFilmsDirector(filmId, directors);
    }

    public void loadDirectors(List<Film> films) {
        directorStorage.loadDirectors(films);
    }
}
