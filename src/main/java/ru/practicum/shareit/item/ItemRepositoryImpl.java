package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();

    private static long generatorId = 0;

    @Override
    public Item save(Item item) {
        item.setId(++generatorId);
        items.compute(item.getOwnerId(), (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
    }

    @Override
    public Item updateItem(long userId, ItemDto itemDto, long itemId) {
        Item updatedItem = null;
        if (items.get(userId) == null) {
            throw new NoSuchElementException();
        }
        for (Item item : items.get(userId)) {
            if (item.getId() == itemId) {
                updatedItem = item;
            }
        }
        if (updatedItem == null) {
            throw new NoSuchElementException();
        }

        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        return updatedItem;
    }

    @Override
    public Item findByUserIdAndItemId(long userId, long itemId) {
        List<Item> allItems = items.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        return allItems.stream().filter(item -> itemId == item.getId()).findFirst().orElse(null);
    }

    @Override
    public List<Item> getSearchedItems(String text) {
        Set<Item> allItems = new HashSet<>();
        for (List<Item> value : items.values()) {
            allItems.addAll(value);
        }
        List<Item> resultItemList = new ArrayList<>();
        if (text.isBlank() || text.isEmpty()) {
            return resultItemList;
        }
        for (Item item : allItems) {
            if (item.getAvailable()
                    && (item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                resultItemList.add(item);
            }
        }
        return resultItemList;
    }

    @Override
    public List<Item> findByUserId(long ownerId) {
        return items.getOrDefault(ownerId, Collections.emptyList());
    }

    @Override
    public void deleteByUserIdAndItemId(long ownerId, long itemId) {
        if(items.containsKey(ownerId)) {
            List<Item> userItems = items.get(ownerId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }
}