package ru.yandex.practicum.filmorate.service.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorService {
    DirectorStorage directorStorage;

    public List<Director> findAllDirectors() {
        return directorStorage.findAllDirectors();
    }

    public Director findDirectorById(Long id) {
        return directorStorage.findDirectorById(id);
    }

    public Director createDirector(Director director) {
        return directorStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    public void deleteDirectorById(@PathVariable("id") Long id) {
        findDirectorById(id);
        directorStorage.deleteDirectorById(id);
    }

    public void deleteFilmsDirector(Long filmId) {
        directorStorage.deleteFilmsDirector(filmId);
    }

    public void addFilmsDirector(Long filmId, LinkedHashSet<Director> directors) {
        directorStorage.addFilmsDirector(filmId, directors);
    }

    public void loadDirectors(List<Film> films) {
        directorStorage.loadDirectors(films);
    }
}
