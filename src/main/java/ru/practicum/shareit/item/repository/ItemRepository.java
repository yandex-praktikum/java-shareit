package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    public Item create(Item item);
}
