package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> findAllGenre() {
        return genreStorage.findAllGenre();
    }

    public Genre findGenreById(Long genreId) {
        return genreStorage.findGenreById(genreId);
    }

    public void addFilmsGenre (Long filmId, LinkedHashSet<Genre> genres) {
        genreStorage.addFilmsGenre(filmId, genres);
    }

    public void deleteFilmsGenre(Long filmId){
        genreStorage.deleteFilmsGenre(filmId);
    }

    public void loadGenres(List<Film> films) {
        genreStorage.loadGenres(films);
    }
}
