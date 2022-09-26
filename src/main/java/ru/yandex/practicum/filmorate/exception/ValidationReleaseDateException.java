package ru.yandex.practicum.filmorate.exception;

public class ValidationReleaseDateException extends RuntimeException {
    public ValidationReleaseDateException(final String message) {
        super(message);
    }
}
