package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(long userId);

    ItemDto getItem(long userId);

    ItemDto addNewItem(long userId, ItemDto item);

    void deleteItem(long userId, long itemId);

    ItemDto changeItem(long userId, long itemId, ItemDto item);

    List<ItemDto> searchItem(String textToSearch);

}
