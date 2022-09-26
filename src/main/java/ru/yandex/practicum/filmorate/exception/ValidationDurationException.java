package ru.yandex.practicum.filmorate.exception;

public class ValidationDurationException extends RuntimeException {
    public ValidationDurationException(final String message) {
        super(message);
    }
}
