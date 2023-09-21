package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    Long id;

    Long requestId;

    String name;

    String description;

    Boolean available;
}
