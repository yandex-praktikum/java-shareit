package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Вещь не найдена."));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(long userId) {
        userService.getUserById(userId);
        return itemRepository.getItemsByOwner(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySearch(String text) {
        return itemRepository.getItemBySearch(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto saveNewItem(ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        return ItemMapper.toItemDto(itemRepository.saveNewItem(ItemMapper.toItem(itemDto), userId));
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item item = itemRepository.getItemById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Вещь не найдена."));
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (item.getOwnerId() == userId) {
            if (name != null && !name.isBlank()) {
                item.setName(name);
            }
            if (description != null && !description.isBlank()) {
                item.setDescription(description);
            }
            if (available != null) {
                item.setAvailable(available);
            }
        } else {
            throw new NotOwnerException(String.format("Пользователь с id %s не является собственником %s", userId, name));
        }
        return ItemMapper.toItemDto(item);
    }
}
