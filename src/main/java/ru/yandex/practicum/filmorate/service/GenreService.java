package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> findAllGenre() {
        return genreDbStorage.findAllGenre();
    }

    public Genre findGenreById(Long genreId) {
        return genreDbStorage.findGenreById(genreId);
    }

    public void addFilmsGenre (Long filmId, LinkedHashSet<Genre> genres) {
        genreDbStorage.addFilmsGenre(filmId, genres);
    }

    public void deleteFilmsGenre(Long filmId){
        genreDbStorage.deleteFilmsGenre(filmId);
    }

    public void addGenres(List<Film> films) {
        genreDbStorage.addGenres(films);
    }
}
