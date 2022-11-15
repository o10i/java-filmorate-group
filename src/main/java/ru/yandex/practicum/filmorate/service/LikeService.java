package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class LikeService {

    private final LikeDbStorage likeDbStorage;

    private final FilmService filmService;

    @Autowired
    public LikeService(LikeDbStorage likeDbStorage, FilmService filmService) {
        this.likeDbStorage = likeDbStorage;
        this.filmService = filmService;
    }

    public void addLike (Long userId, Long filmId) {
        filmService.validatorId(filmId);
        likeDbStorage.addLike(userId, filmId);
    }

    public void deleteLike (Long userId, Long filmId) {
        filmService.validatorId(filmId);
        likeDbStorage.deleteLike(userId, filmId);
    }

    public List<Film> getTopFilms(Integer count) {
        return filmService.getTopFilms(count);
    }
}
