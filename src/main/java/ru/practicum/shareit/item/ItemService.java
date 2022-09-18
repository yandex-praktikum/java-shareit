package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(Long userId, ItemDto itemDto);

    Item update(Long userId, Long id, ItemDto itemDto);

    void delete(Long id, Long userId);

    Item find(Long id);

    List<Item> findUserItems(Long userId);

    List<Item> findItemsByText(String text);

}
