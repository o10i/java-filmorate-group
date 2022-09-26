package ru.yandex.practicum.filmorate.exception;

public class ValidationFilmNameException extends RuntimeException {
    public ValidationFilmNameException(final String message) {
        super(message);
    }
}
