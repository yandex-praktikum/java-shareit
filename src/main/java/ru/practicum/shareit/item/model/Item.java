package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Item {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Boolean available;
}
