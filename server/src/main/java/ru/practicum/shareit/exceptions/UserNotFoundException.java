package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "UserNotFoundException")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
