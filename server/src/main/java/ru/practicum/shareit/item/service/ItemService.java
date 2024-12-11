package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(int userId, ItemSaveDto itemSaveDto);

    CommentDto addComment(int userId, int itemId, CommentSaveDto commentSaveDto);

    ItemDto updateItem(int userId, int itemId, ItemSaveDto itemSaveDto);

    ItemDto getItem(int itemId);

    Collection<ItemDto> getAllOwnerItems(int userId);

    Collection<ItemDto> searchItems(String text);
}
