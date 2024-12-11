package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemRequestDto {
    private int id;

    private String description;

    private User requester;

    private LocalDateTime created;

    private Collection<ItemResponseToRequestDto> items;
}
