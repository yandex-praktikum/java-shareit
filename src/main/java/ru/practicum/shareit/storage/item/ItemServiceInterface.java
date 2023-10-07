package ru.practicum.shareit.storage.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemServiceInterface {
    ItemDto addItem(ItemDto itemDto, int sharerUserId);

    ItemDto updateItem(ItemDto itemDto, int sharerUserId, int itemId);

    ItemDto getItemById(int id);

    List<Item> getAllItemsByOwnerId(int id);

    List<Item> searchItem(String keyWord);
}
