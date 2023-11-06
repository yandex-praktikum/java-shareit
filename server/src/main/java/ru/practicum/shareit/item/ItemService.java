package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemService {

    @Transactional
    ItemDto create(ItemDto itemDto, Long userId);

    @Transactional
    ItemDto update(ItemUpdateDto itemDto, Long userId, Long itemId);

    @Transactional
    ItemDtoWithBookingDates findItemById(Long id, Long userId);

    List<ItemDto> getAll();

    @Transactional
    List<ItemDtoWithBookingDates> getAllByUserId(Long id, Long from, Long size);

    @Transactional
    List<ItemDto> search(String text, Long from, Long size);

    @Transactional
    CommentFullDto createComment(CommentDto comment, Long itemId, Long userId);
}
