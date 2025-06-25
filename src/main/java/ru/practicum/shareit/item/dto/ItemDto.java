package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;

    private Boolean available;

    //  private long owner; //оставил на всякий случай

    private long request;
}
