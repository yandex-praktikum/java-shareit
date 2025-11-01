package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    //описание
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Статус должен быть указан")
    private boolean available;

    //владелец вещи
    private User owner;

    //запрос
    private ItemRequest request;
}
