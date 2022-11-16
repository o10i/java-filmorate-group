package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

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
        filmService.findFilmById(filmId);
        validator(userId, filmId);
        likeDbStorage.addLike(userId, filmId);
    }

    public void deleteLike (Long userId, Long filmId) {
        filmService.findFilmById(filmId);
        validator(userId, filmId);
        likeDbStorage.deleteLike(userId, filmId);
    }

    private void validator (Long userId, Long filmId) {
        if (likeDbStorage.LikeFromUser(filmId, userId) == 0) {
            throw new UserNotFoundException(String.format("User with %d id not found", filmId));
        }
    }
}
