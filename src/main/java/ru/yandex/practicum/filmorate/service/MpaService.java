package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {

    private final MpaDbStorage mpaDbStorage;
    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> findAllMPA(){
        return mpaDbStorage.findAllMPA();
    }

    public Mpa findMPAById(Long MPAId){
        validatorMPA(MPAId);
        return mpaDbStorage.findMPAById(MPAId);
    }

    private void validatorMPA(Long mpaId) {
        if (mpaDbStorage.findMPAById(mpaId) == null || mpaId == null) {
            throw new DataNotFoundException(String.format("MPA with %d id not found", mpaId));
        }
    }
}
