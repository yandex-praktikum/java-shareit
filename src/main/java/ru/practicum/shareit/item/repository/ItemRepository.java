package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAllItems(int userId);

    ItemDto add(int userId, Item item);

    ItemDto update(int userId, int id, ItemDto itemDto);

    Optional<ItemDto> findById(int userId, int id);

    void delete(int userId, int id);

    List<ItemDto> findItems(String searchRequest);

}
