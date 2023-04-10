package ru.practicum.shareit.request;

import lombok.Value;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class ItemRequest {
    long id;

    @NotNull(message = "Description cannot be null")
    @Size(max = 300, message = "Description must be shorter than 300 characters")
    String description;

    @NotNull(message = "Requestor cannot be null")
    User requestor;

    @NotNull(message = "Request created date cannot be null")
    @PastOrPresent(message = "Request created date cannot be in the future")
    LocalDateTime created;
}
