package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item save(Item item);

    Item update(Item item);


    Item get(int itemId);

    List<Item> getOwnerItems(int ownerId);

    List<Item> find(String text);
}
