package ru.practicum.shareit.exceptions;

public class CommentAuthorHaveNoBookingsException extends RuntimeException {
    public CommentAuthorHaveNoBookingsException(String message) {
        super(message);
    }
}