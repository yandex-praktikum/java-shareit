package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DublicatedDataException")
public class DublicatedDataException extends RuntimeException {
    public DublicatedDataException(String message) {
        super(message);
        log.error(message);
    }
}
