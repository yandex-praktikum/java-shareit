package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;

import java.util.List;

public interface ItemService {

    ItemOutputDto getItem(Long id, Long userId);

    List<ItemOutputDto> getOwnItems(Long ownerId);

    ItemInputDto create(Long ownerId, ItemInputDto itemInputDto);

    ItemInputDto update(Long ownerId, Long itemId, ItemInputDto itemInputDto);

    List<ItemInputDto> searchItem(String text);

    CommentDto createComment(Long bookerId, Long itemId, CommentDto commentDto);

}
