package ru.yandex.practicum.filmorate.controller.film;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.LikeService;

@RestController
@RequestMapping("/films/{id}/like/{userId}")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PutMapping
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        likeService.addLike(userId, filmId);
    }

    @DeleteMapping
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        likeService.deleteLike(userId, filmId);
    }
}
