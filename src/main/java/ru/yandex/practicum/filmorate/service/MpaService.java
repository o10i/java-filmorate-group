package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> findAllMPA(){
        return mpaStorage.findAllMPA();
    }

    public Mpa findMPAById(Long MPAId) {
        return mpaStorage.findMPAById(MPAId);
    }
}
