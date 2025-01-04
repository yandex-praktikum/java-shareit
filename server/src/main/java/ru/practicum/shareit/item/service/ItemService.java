package ru.practicum.shareit.item.service;

import ru.practicum.shareit.base.service.BaseService;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.RequestCreateCommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.RequestItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService extends BaseService<Item, Long> {
    ItemDto create(Long userId, RequestItemDto item);

    ItemDto update(Long userId, Long itemId, RequestItemDto item);

    ItemDto findItemById(Long itemId);

    Collection<ItemDto> findAllItemsByUserId(Long userId);

    Collection<ItemDto> searchByNameAndDescription(String text);

    CommentDto saveCommentToItem(Long userId, Long itemId, RequestCreateCommentDto requestCreateCommentDto);
}
