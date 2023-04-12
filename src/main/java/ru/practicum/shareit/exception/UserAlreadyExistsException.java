package ru.practicum.shareit.exception;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserAlreadyExistsException extends Throwable {
    public UserAlreadyExistsException(@Email @NotBlank String s) {
    }
}
