package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    @EqualsAndHashCode.Exclude
    private Boolean available;
    private Long owner;
}