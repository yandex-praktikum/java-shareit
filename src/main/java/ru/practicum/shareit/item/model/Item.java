package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private final Long id;
    private String name;
    private String description;
    private Boolean available;
}
