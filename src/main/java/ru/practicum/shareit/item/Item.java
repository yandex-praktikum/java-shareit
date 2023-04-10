package ru.practicum.shareit.item;

import lombok.Value;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class Item {
    long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must be shorter than 100 characters")
    String name;

    @NotNull(message = "Description cannot be null")
    @Size(max = 300, message = "Description must be shorter than 300 characters")
    String description;

    @NotNull(message = "Description cannot be null")
    boolean available;

    @NotNull(message = "Owner cannot be null")
    User owner;

    ItemRequest request;
}
