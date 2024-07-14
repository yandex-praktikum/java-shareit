package ru.practicum.shareit.item.service.impl;


import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.exception.InvalidItemDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.ItemService;


import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import ru.practicum.shareit.user.repository.UserStorage;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User owner = userStorage.findById(userId);
        if (owner == null) {
            throw new UserNotFoundException("User not found");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new InvalidItemDataException("Item name cannot be empty");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new InvalidItemDataException("Item description cannot be empty");
        }
        if (itemDto.getAvailable() == null) {
            throw new InvalidItemDataException("Item availability status cannot be null");
        }
        Item item = ItemMapper.toEntity(itemDto, owner, null);
        item = itemStorage.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemStorage.findById(itemId);
        if (item == null || !item.getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("User is not the owner of the item or item not found");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        item = itemStorage.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = itemStorage.findById(itemId);
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        List<Item> items = itemStorage.findByOwnerId(userId);
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemStorage.search(text);
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}