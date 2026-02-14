package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.InMemoryItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorage itemStorage;
    private final InMemoryUserStorage userStorage;
    private final InMemoryItemRequestStorage requestStorage;

    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userStorage.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + ownerId + " не найден"));

        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = requestStorage.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос вещи с id " + itemDto.getRequestId() + " не найден"));
        }

        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(owner);
        item.setRequest(request);

        Item createdItem = itemStorage.create(item);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto) {
        Item existingItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (!existingItem.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Пользователь с id " + ownerId + " не является владельцем вещи с id " + itemId);
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemStorage.update(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        userStorage.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + ownerId + " не найден"));

        return itemStorage.findByOwner(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
