package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ItemResponseToRequestDto {
    private int id;
    private int ownerId;
    private String name;
}
