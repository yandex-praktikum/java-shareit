package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto item);

    ItemDto updateItem(Long userId, Long itemId, ItemDto item);

    ItemWithCommentDto getItem(Long itemId);

    List<ItemWithCommentDto> getUserItems(Long userId);

    List<ItemDto> searchItems(Long userId, String text);
}
