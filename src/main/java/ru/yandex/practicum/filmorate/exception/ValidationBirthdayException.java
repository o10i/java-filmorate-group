package ru.yandex.practicum.filmorate.exception;

public class ValidationBirthdayException extends RuntimeException{
    public ValidationBirthdayException(final String message) {
        super(message);
    }
}
