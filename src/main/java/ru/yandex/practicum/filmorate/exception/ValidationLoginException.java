package ru.yandex.practicum.filmorate.exception;

public class ValidationLoginException  extends RuntimeException {
    public ValidationLoginException(final String message) {
        super(message);
    }
}
