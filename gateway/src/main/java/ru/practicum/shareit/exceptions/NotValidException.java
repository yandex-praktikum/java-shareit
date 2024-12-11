package ru.practicum.shareit.exceptions;

public class NotValidException extends RuntimeException {

    public NotValidException(Class<?> entity, String reason) {
        super(entity.getSimpleName() + " " + reason);
    }
}