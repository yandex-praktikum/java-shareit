package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс для хранения объектов вещей
 */
@Component
public interface ItemStorage {
    List<ItemDto> getItems(long userId);

    ItemDto getItemById(long id);

    ItemDto getItemByIdAndUserId(long userId, long itemId);

    ItemDto add(long userId, ItemDto itemDto);

    ItemDto update(long id, ItemDto ItemDtoExisting, ItemDto itemDto);

    void delete(long userId, long itemId);

    List<ItemDto> search(String text);
}
