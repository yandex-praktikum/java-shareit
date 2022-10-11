package ru.practicum.shareit.item.requests.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class ItemRequestDto {

    private Integer id;

    @NotBlank(message = "description не может быть пустым")
    @NotNull
    private String description;

    private User requestor;

    private LocalDateTime created = LocalDateTime.now();

    private Set<Item> items = new HashSet<>();
}
