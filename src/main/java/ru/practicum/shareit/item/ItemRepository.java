package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item add(Long userId, Item item);

    Item update(Long userId, ItemDto itemDto, Long itemId);

    List<Item> getAllForUser(Long userId);

    ItemDto getOneWithoutOwner(Long itemId);

    List<ItemDto> search(Long userId, String text);

    void delete(Long userId, Long itemId);

    void containsSameOwner(Long userId, Long itemId);

    void containsById(Long itemId);
    Item getOne(Long itemId);
}
