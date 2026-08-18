package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import java.util.Collection;


public interface ItemService {

    ItemDto create(Long ownerId, ItemCreateDto itemDto);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long itemId, Long userId);

    Collection<ItemDto> getItemsByOwner(Long ownerId);

    Collection<ItemDto> search(String text);

    CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}
