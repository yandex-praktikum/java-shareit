package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    private Long id;
    private String name;
    private String description;

    private Boolean available;

    private long owner;

    private long request;
}
