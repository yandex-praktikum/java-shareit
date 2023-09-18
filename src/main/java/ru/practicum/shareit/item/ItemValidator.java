package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemValidator {
    // Метод, проверяющий, что все обязательные поля ItemDto не null
    public static void checkAllFields(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank())
            throw new ValidationException("Имя вещи не может быть пустым");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank())
            throw new ValidationException("Описание вещи не может быть пустым");
        if (itemDto.getAvailable() == null)
            throw new ValidationException("Статус доступности вещи не может быть пустым");
    }

    // Метод, проверяющий, что введенные поля ItemDto валидны (для запросов на обновление вещи)
    public static void checkNotNullFields(ItemDto itemDto) {
        if (itemDto.getName() != null && itemDto.getName().isBlank())
            throw new ValidationException("Имя вещи не может быть пустым");
        if (itemDto.getDescription() != null && itemDto.getDescription().isBlank())
            throw new ValidationException("Описание вещи не может быть пустым");
        // У статуса доступности всего лишь два состояния: True/False - в проверке не нуждается
    }
}
