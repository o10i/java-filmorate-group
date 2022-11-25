package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.review.impl.ReviewDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewDbTest {
    JdbcTemplate jdbcTemplate;
    ReviewDbStorage reviewStorage;
    ReviewLikeStorage likeStorage;
    FilmService filmService;
    UserService userService;

    @BeforeAll
    public void beforeAll() {
        userService.createUser(User.builder()
                .email("1@ya.ru")
                .login("bot")
                .name("Vova")
                .birthday(LocalDate.of(2022, 1, 1))
                .build());
        filmService.createFilm(Film.builder()
                .name("Iron Man")
                .description("Some superhero's shit")
                .releaseDate(LocalDate.of(2012, 8, 12))
                .duration(120)
                .mpa(Mpa.builder().id(2L).name("PG").build())
                .build());
    }

    private Review getReview() {
        return Review.builder()
                .content("good review")
                .positive(true)
                .filmId(1L)
                .userId(1L)
                .useful(0L)
                .build();
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update("DELETE FROM reviews");
        jdbcTemplate.update("ALTER TABLE REVIEWS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void testCreateReview() {
        var review = getReview();
        reviewStorage.createReview(review);
        assertEquals(review, reviewStorage.findReviewById(review.getReviewId()));
    }

    @Test
    public void testUpdateReview() {
        var review = getReview();
        reviewStorage.createReview(review);
        review.setContent("updated good review");
        reviewStorage.updateReview(review);
        assertEquals(review, reviewStorage.findReviewById(review.getReviewId()));
    }

    @Test
    public void testFindReviewByWrongId() {
        assertThrows(DataNotFoundException.class, () -> reviewStorage.findReviewById(-1L));
    }

    @Test
    public void testDeleteReview() {
        var review = getReview();
        reviewStorage.createReview(review);
        reviewStorage.deleteReview(review.getReviewId());
        assertEquals(0, reviewStorage.findAllReviews().size());
        assertThrows(DataNotFoundException.class, () -> reviewStorage.findReviewById(review.getReviewId()));
    }

    @Test
    public void testFindAllReviews() {
        var review = getReview();
        var review1 = getReview();
        var review2 = getReview();
        List<Review> values = new ArrayList<>();

        values.add(reviewStorage.createReview(review));
        values.add(reviewStorage.createReview(review1));
        values.add(reviewStorage.createReview(review2));

        assertEquals(values, reviewStorage.findAllReviews());
    }

    @Test
    public void testFindAllReviewsWithCount() {
        var review = reviewStorage.createReview(getReview());
        var review1 = reviewStorage.createReview(getReview());
        reviewStorage.createReview(getReview());
        List<Review> values = new ArrayList<>();

        values.add(review);
        values.add(review1);

        assertEquals(values, reviewStorage.findAllReviews(1L, 2));
    }

    @Test
    public void testAddReviewLikeAndDelete() {
        var review = reviewStorage.createReview(getReview());
        var user = userService.findUserById(1L);

        likeStorage.addLike(review, user);

        assertTrue(likeStorage.containsLike(review, user, true));
        assertEquals(1, reviewStorage.findReviewById(1L).getUseful());

        likeStorage.removeLike(review, user);

        assertFalse(likeStorage.containsLike(review, user, true));
        assertEquals(0, reviewStorage.findReviewById(1L).getUseful());
    }

    @Test
    public void testAddReviewDislikeAndDelete() {
        var review = reviewStorage.createReview(getReview());
        var user = userService.findUserById(1L);

        likeStorage.addDislike(review, user);

        assertTrue(likeStorage.containsLike(review, user, false));
        assertEquals(-1, reviewStorage.findReviewById(1L).getUseful());

        likeStorage.removeDislike(review, user);

        assertFalse(likeStorage.containsLike(review, user, false));
        assertEquals(0, reviewStorage.findReviewById(1L).getUseful());
    }
}
