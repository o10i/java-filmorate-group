package ru.yandex.practicum.filmorate.storage.review;

public interface ReviewLikeStorage {
    Long getUseful(Long reviewId);
    void addLike(Long reviewId, Long userId);
    void addDislike(Long reviewId, Long userId);
    void removeLike(Long reviewId, Long userId);
    void removeDislike(Long reviewId, Long userId);
}
