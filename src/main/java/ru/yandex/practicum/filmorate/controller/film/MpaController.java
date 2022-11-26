package ru.yandex.practicum.filmorate.controller.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaController {

    MpaService mpaService;

    @GetMapping
    public List<Mpa> findAllMPA() {
        return mpaService.findAllMPA();
    }

    @GetMapping("/{id}")
    public Mpa findMPAById(@PathVariable("id") Long mpaId) {
        return mpaService.findMPAById(mpaId);
    }
}
