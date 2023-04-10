package ru.practicum.shareit.booking;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class BookingStatus {
    int id;

    @NotBlank(message = "Status name cannot be blank")
    @Size(max = 100, message = "Status name must be shorter than 100 characters")
    String name;

    @Size(max = 300, message = "Description must be shorter than 300 characters")
    String description;
}
