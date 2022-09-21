package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemNotAvalibleException extends RuntimeException {
    public ItemNotAvalibleException(String message) {
        super(message);
    }
}
