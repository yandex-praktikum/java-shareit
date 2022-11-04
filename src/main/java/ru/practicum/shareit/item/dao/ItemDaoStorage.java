package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDaoStorage {

    Item addToUserItemsList(Long userId, Item item);

    Optional<Item> update(Long userId, Long itemId, Item item);

    Optional<Item> getItemById(Long userId, Long itemId);

    Optional<Item> getItemByIdForAllUser(Long itemId);

    List<Item> findAll(Long userId);

    List<Item> findItemByText(String text);

}
