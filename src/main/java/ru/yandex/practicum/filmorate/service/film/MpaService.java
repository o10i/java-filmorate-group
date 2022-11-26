package ru.yandex.practicum.filmorate.service.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaService {

    MpaStorage mpaStorage;

    public List<Mpa> findAllMPA() {
        return mpaStorage.findAllMPA();
    }

    public Mpa findMPAById(Long MPAId) {
        return mpaStorage.findMPAById(MPAId);
    }
}
