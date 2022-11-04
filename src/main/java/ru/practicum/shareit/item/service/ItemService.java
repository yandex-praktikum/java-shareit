package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    ItemDto create(Long userId, ItemDto dto);

    Optional<ItemDto> update(Long userId, Long itemId, ItemDto dto);

    Optional<ItemDto> getItemByIdForUser(Long userId, Long itemId);

    Optional<ItemDto> getItemByIdForAllUser(Long itemId);

    List<ItemDto> findAll(Long userId);

    List<ItemDto> findItemByText(String text);

}
