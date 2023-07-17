package ru.practicum.shareit.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundRecordInBD extends RuntimeException {

    public NotFoundRecordInBD(String message) {
        super(message);
    }
}
