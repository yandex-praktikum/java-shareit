package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Collection<ItemDto> findAll(Long userId);

    Optional<ItemDto> findItem(Long itemId);

    Optional<ItemDto> findItemForUpdate(Long userId, Long itemId);

    List<ItemDto> searchItem(String text);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, Item item);
}
