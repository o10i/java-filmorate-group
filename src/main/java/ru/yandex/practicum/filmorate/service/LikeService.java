package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeStorage likeStorage;

    private final UserService userService;

    private final FilmService filmService;



    public void addLike (Long userId, Long filmId) {
        filmService.findFilmById(filmId);
        userService.findUserById(userId);
        likeStorage.addLike(userId, filmId);
    }

    public void deleteLike (Long userId, Long filmId) {
        filmService.findFilmById(filmId);
        userService.findUserById(userId);
        if (likeStorage.getLikes(userId, filmId) == null) {
            throw new DataNotFoundException("Like not found");
        }
        likeStorage.deleteLike(userId, filmId);
    }
}
