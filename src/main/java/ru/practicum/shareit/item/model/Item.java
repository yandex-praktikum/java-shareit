package ru.practicum.shareit.item.model;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


import javax.validation.constraints.NotNull;


@Data
@Builder
public class Item {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private boolean available;
    @NotNull
    private User owner;
    private ItemRequest request;
}

