package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item add(ItemDto itemDto, int ownerId);

    Item update(ItemDto itemDto, int itemId, int ownerId);

    Item get(int itemId);

    List<Item> getOwnerItems(int ownerId);

    List<Item> find(String text);
}
