package ru.practicum.shareit.exception;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ErrorResponse {
    @NotBlank
    private final String error;
    @NotBlank
    private final String description;
}
