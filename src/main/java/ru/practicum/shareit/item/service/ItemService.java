package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, UserDto userDto);

    Item findByUserIdAndItemId(Long userId, Long itemId);

    ItemDto findById(Long itemId);

    List<ItemDto> findAllByUserId(Long userId);

    List<ItemDto> findAllByText(String text);

    ItemDto update(Long userId, Long itemId, ItemDto item);
}