package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemStorageImpl implements ItemStorage {
    private long counter = 1;
    private final Map<Long, Collection<Item>> items = new HashMap<>();

    @Override
    public Collection<ItemDto> findAll(Long userId) {
        Collection<Item> userItems = items.getOrDefault(userId, Collections.emptyList());
        return userItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDto> findItem(Long itemId) {
        List<Item> allItems = new ArrayList<>();
        items.forEach((user, items1) -> allItems.addAll(items1));
        return allItems.stream()
                .filter(item1 -> item1.getId() == itemId)
                .findFirst()
                .map(ItemMapper::toItemDto);
    }

    @Override
    public Optional<ItemDto> findItemForUpdate(Long userId, Long itemId) {
        return items.getOrDefault(userId, Collections.emptyList()).stream()
                .filter(item1 -> item1.getId() == itemId)
                .findFirst()
                .map(ItemMapper::toItemDto);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        List<Item> allItems = new ArrayList<>();
        items.forEach((userId, items1) -> allItems.addAll(items.get(userId)));
        return allItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        itemDto.setId(counter++);
        Item item = ItemMapper.toItem(itemDto, userId);
        items.compute(userId, (id, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item item) {
        Item repoItem = items.get(userId).stream()
                .filter(item1 -> item1.getId() == itemId)
                .findFirst().orElseThrow(() -> {
                    log.warn("Вещь с itemId{} не найдена", itemId);
                    throw new ObjectNotFoundException("Вещь не найдена");
                })
                ;
            if (item.getName() != null) {
                repoItem.setName(item.getName());
        }
            if (item.getDescription() != null) {
                repoItem.setDescription(item.getDescription());
        }
            if (item.getAvailable() != null) {
                repoItem.setAvailable(item.getAvailable());
        }
        items.get(userId).removeIf(item1 -> item1.getId() == itemId);
        items.get(userId).add(repoItem);
        return ItemMapper.toItemDto(repoItem);
    }
}
