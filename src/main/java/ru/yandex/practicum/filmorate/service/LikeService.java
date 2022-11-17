package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

@Service
public class LikeService {

    private final LikeDbStorage likeDbStorage;

    private final UserService userService;

    private final FilmService filmService;

    @Autowired
    public LikeService(LikeDbStorage likeDbStorage, UserService userService, FilmService filmService) {
        this.likeDbStorage = likeDbStorage;
        this.userService = userService;
        this.filmService = filmService;
    }

    public void addLike (Long userId, Long filmId) {
        filmService.findFilmById(filmId);
        userService.findUserById(userId);
        likeDbStorage.addLike(userId, filmId);
    }

    public void deleteLike (Long userId, Long filmId) {
        filmService.findFilmById(filmId);
        userService.findUserById(userId);
        if (likeDbStorage.getLikes(userId, filmId) == null) {
            throw new DataNotFoundException("Like not found");
        }
        likeDbStorage.deleteLike(userId, filmId);
    }
}
