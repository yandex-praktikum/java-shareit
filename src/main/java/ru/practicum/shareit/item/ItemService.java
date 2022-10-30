package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(Long userId, ItemDto dto);
    Optional<Item> update(Long userId, Long itemId, Item item);
    Optional<Item> getItemByIdForUser(Long userId, Long itemId);
    Optional<Item> getItemByIdForAllUser(Long itemId);
    List<Item> findAll(Long userId);
    List<Item> findItemByText(String text);
}
