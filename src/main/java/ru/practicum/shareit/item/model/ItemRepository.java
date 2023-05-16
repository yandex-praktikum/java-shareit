package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {

    ItemDto addItem(Item item, long ownerId);

    ItemDto updateItem(Item item, long ownerId, long itemId);

    ItemDto getItem(long itemId);

    List<ItemDto> getUsersOwnItems(long ownerId);

    List<ItemDto> searchItemByDescription(String searchText);

}