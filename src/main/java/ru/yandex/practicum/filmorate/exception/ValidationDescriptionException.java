package ru.yandex.practicum.filmorate.exception;

public class ValidationDescriptionException extends RuntimeException {
    public ValidationDescriptionException(final String message) {
        super(message);
    }
}
