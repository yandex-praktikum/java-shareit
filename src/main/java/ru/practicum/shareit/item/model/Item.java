package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
}

