package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DataDbStorage;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Service
public class DataService {

    private final DataDbStorage dataDbStorage;

    @Autowired
    public DataService(DataDbStorage dataDbStorage) {
        this.dataDbStorage = dataDbStorage;
    }

    public List<Genre> findAllGenre() {
        return dataDbStorage.findAllGenre();
    }

    public Genre findGenreById(Long genreId){
        validatorGenre(genreId);
        return dataDbStorage.findGenreById(genreId);
    }

    public List<MPA> findAllMPA(){
        return dataDbStorage.findAllMPA();
    }

    public MPA findMPAById(Long MPAId){
        validatorMPA(MPAId);
        return dataDbStorage.findMPAById(MPAId);
    }

    private void validatorMPA(Long mpaId) {
        if (dataDbStorage.findMPAById(mpaId) == null || mpaId == null) {
            throw new DataNotFoundException(String.format("MPA with %d id not found", mpaId));
        }
    }

    private void validatorGenre(Long genreId) {
        if (dataDbStorage.findGenreById(genreId) == null || genreId == null) {
            throw new DataNotFoundException(String.format("Genre with %d id not found", genreId));
        }
    }


}
