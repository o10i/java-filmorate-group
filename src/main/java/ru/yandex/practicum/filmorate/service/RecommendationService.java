package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.recommendation.RecommendationStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationStorage recommendationStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final DirectorService directorService;

    public List<Film> getRecommendations(Long userId) {
        userService.findUserById(userId);
        List<Film> films = recommendationStorage.getRecommendations(userId);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }
}
