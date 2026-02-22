package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {
    List<ItemDto> findAllByUser(Long userId);

    ItemDto findById(Long id);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    void delete(Long id);

    List<ItemDto> search(String text);
}