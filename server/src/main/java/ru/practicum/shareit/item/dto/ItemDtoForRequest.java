package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemDtoForRequest {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private int requestId;
}
