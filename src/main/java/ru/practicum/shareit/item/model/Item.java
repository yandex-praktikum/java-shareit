package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    Long id;

    Long ownerId;

    Long requestId;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;
}
