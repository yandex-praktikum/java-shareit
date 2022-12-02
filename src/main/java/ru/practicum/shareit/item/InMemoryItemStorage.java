package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    private static int generateId = 1;

    private final HashMap<Integer, Item> items = new HashMap<>();

    private static Integer getNextId() {
        return generateId++;
    }

    @Override
    public ItemDto addItem(int userId, ItemDto itemDto) {
        Item item = ItemMapper.itemFromItemDto(itemDto, userId, getNextId());
        items.put(item.getId(), item);
        return ItemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) {
        items.put(itemId, ItemMapper.itemFromItemDto(itemDto, userId, itemId));
        return ItemMapper.itemToItemDto(items.get(itemId));
    }

    @Override
    public Item findItemById(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public ItemDto findItemDtoById(Integer itemId) {
        return ItemMapper.itemToItemDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> findAllItemsForUser(int userId) {
        return items
                .values()
                .stream()
                .filter(x -> x.getUserId() == userId)
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAllItems() {
        return new ArrayList<>(items.values());
    }
}
