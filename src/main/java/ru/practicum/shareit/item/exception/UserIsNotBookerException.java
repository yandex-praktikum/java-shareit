package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsNotBookerException extends RuntimeException {
    public UserIsNotBookerException(String message) {
        super(message);
    }
}
