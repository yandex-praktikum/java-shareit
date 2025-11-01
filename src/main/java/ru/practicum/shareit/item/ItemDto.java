package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * TODO Sprint add-controllers.
 * DTO для Item - то что увидит пользователь
 */
@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    //описание
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Статус должен быть указан")
    private Boolean available;

    //id владельца вещи
    private Long ownerId;

    //id запроса
    private Long requestId;

}
