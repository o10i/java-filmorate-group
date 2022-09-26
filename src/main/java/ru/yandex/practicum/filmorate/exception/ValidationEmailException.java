package ru.yandex.practicum.filmorate.exception;

public class ValidationEmailException extends RuntimeException {
    public ValidationEmailException(final String message) {
        super(message);
    }
}
