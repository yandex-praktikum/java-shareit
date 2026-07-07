package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemRequestDto {
    @NotNull(groups = OnCreate.class, message = "Название обязательно.")
    @NotBlank(groups = OnCreate.class, message = "Название не может быть пустым.")
    @Size(min = 1, max = 255, groups = {OnCreate.class, OnUpdate.class},
            message = "Название должно быть от 1 до 255 символов.")
    private String name;

    @NotNull(groups = OnCreate.class, message = "Описание обязательно.")
    @NotBlank(groups = OnCreate.class, message = "Описание не может быть пустым.")
    @Size(min = 1, max = 1000, groups = {OnCreate.class, OnUpdate.class},
            message = "Описание должно быть от 1 до 1000 символов.")
    private String description;

    @NotNull(groups = OnCreate.class, message = "Доступность должна быть указана.")
    private Boolean available;

    private Long requestId;

    public interface OnCreate {}

    public interface OnUpdate {}
}