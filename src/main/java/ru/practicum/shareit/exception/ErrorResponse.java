package ru.practicum.shareit.exception;

public class ErrorResponse {

    private final String error;


    public String getError() {
        return error;
    }

    public ErrorResponse(String error) {
        this.error = error;
    }
}
