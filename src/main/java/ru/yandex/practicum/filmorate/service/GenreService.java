package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

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

    public Genre findGenreById(Long genreId){
        validatorGenre(genreId);
        return genreDbStorage.findGenreById(genreId);
    }

    private void validatorGenre(Long genreId) {
        if (genreDbStorage.findGenreById(genreId) == null || genreId == null) {
            throw new DataNotFoundException(String.format("Genre with %d id not found", genreId));
        }
    }
}
