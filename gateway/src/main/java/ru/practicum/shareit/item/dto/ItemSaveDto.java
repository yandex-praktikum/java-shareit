package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.api.ValidateUpdateRequest;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemSaveDto {
    @NotNull(message = "Название не может быть пустым.", groups = {ValidateCreateRequest.class})
    @Size(min = 1, max = 50, message = "Название не может быть длиннее 50 символов.",
            groups = {ValidateCreateRequest.class, ValidateUpdateRequest.class})
    String name;

    @NotNull(message = "Описание не может быть пустым.", groups = {ValidateCreateRequest.class})
    @Size(min = 1, max = 300, message = "Описание не может быть длиннее 300 символов.",
            groups = {ValidateCreateRequest.class, ValidateUpdateRequest.class})
    String description;

    @NotNull(message = "Доступность не может быть пустым.", groups = {ValidateCreateRequest.class})
    Boolean available;

    Integer requestId;
}
