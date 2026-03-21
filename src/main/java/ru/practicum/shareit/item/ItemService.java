package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {
    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> search(String text);

    ItemDto getById(Long itemId);

    ItemDto create(Long userId, ItemDto itemDto);

    List<ItemDto> getByOwner(Long userId);
}