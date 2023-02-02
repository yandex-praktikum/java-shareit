package ru.practicum.item;

import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;

import java.util.List;

public interface ItemService {

    ItemDtoResponse getItemById(Long userId, Long ownerId);

    List<ItemDtoResponse> getAllByUserId(Long id);

    List<ItemDto> getItemsByText(String text);

    ItemDto add(ItemDto item, Long ownerId);

    Item update(ItemDto item, Long itemId, Long id);

    Item delete(ItemDto item, Long ownerId);

    CommentDto addComment(CommentDto commentDto, Long userId, Long itemId);
}
