package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        userStorage.getById(userId).orElseThrow(() -> {
            log.warn("User not found");
            throw new ObjectNotFoundException("User not found");
        });
        log.info("Item created");
        return itemStorage.create(userId, itemDto);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item item) {
        itemStorage.findItemForUpdate(userId, itemId).orElseThrow(() -> {
            log.warn("Item not found for update");
            throw new ObjectNotFoundException("Item not found for update");
        });
        log.info("Item updated");
        return itemStorage.update(userId, itemId, item);
    }

    @Override
    public ItemDto findItem(Long itemId) {
        log.info("Item sent");
        return itemStorage.findItem(itemId).orElseThrow(() -> {
            log.warn("Item not found");
            throw new ObjectNotFoundException("Item not found");
        });
    }

    @Override
    public Collection<ItemDto> findAll(Long userId) {
        log.info("Items sent");
        return itemStorage.findAll(userId);
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        log.info("Search results sent");
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.searchItem(text);
    }
}
