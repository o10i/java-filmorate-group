package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.LikeService;

@RestController
@RequestMapping("/films/{id}/like/{userId}")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        likeService.addLike(userId, filmId);
    }

    @DeleteMapping
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        likeService.deleteLike(userId, filmId);
    }
}
