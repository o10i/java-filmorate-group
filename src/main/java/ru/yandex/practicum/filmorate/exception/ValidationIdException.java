package ru.yandex.practicum.filmorate.exception;

public class ValidationIdException extends RuntimeException {
    public ValidationIdException(final String message) {
        super(message);
    }
}