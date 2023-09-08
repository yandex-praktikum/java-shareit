package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemService {

     ItemDto create(ItemDto itemDto, Long id);

     List<ItemDto> getAll(Long id, Integer from, Integer size);

     ItemDto get(Long id, Long userId);

     List<ItemDto> search(String value, Integer from, Integer size);

     ItemDto update(ItemDto itemDto, Long id, Long itemId);

     void delete(Long id, Long itemId);

     @Transactional
     CommentDto addComment(CommentDto commentDto, long userId, long itemId);
}
