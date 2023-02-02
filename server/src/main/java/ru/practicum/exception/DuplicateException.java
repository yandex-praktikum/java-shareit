package ru.practicum.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@RequiredArgsConstructor
public class DuplicateException extends RuntimeException {
    private final String parameter;

    public String getParameter() {
        return parameter;
    }
}
