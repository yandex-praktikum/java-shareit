package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(long userId, long itemId);

    List<ItemDto> getItems(long userId);

    ItemDto add(long userId, ItemDto itemDto) throws NotFoundException;

    ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException;

    List<ItemDto> search(long userId, String searchItem);

    void delete(long userId, long itemId);
}