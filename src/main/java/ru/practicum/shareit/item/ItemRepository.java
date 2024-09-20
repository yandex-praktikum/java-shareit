package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

interface ItemRepository {
    Item addItem(User user, Item item);

    Optional<Item> getItemById(Long itemId);

    Item updateItem(Item item, ItemDto itemDto);

    List<ItemDto> getItemsFromUsers(Long userId);

    List<ItemDto> search(String search);
}
