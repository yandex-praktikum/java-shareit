package ru.practicum.shareit.requests.dto;


import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class ItemRequestDto {

    private Integer id;

    private String description;

    private User requestor;

    private LocalDateTime created = LocalDateTime.now();

    private Set<Item> items = new HashSet<>();
}
