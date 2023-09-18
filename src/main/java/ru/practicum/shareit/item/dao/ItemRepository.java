package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getItems();

    List<Item> getUserItems(Long userId);

    Item getItem(Long itemId);

    Item createItem(Item item);

    Item updateItem(Item updatedItem, Long redactorId);
    //void deleteItem(Long itemId); // Пока по тестам эта фича не предусмотрена
}
