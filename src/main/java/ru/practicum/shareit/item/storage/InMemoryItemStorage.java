package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotOwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    public static final String ITEM_NOT_FOUND = "Не найдена вещь с id: ";
    public static final String USER_NOT_OWNER = "Пользователь не является владельцем вещи";

    private final Map<Long, Item> items = new HashMap<>();
    private long currentId = 1;

    @Override
    public Item createItem(Item item) {
        long id = generateId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        long itemId = item.getId();
        if (!items.containsKey(itemId)) {
            throw new ItemNotFoundException(ITEM_NOT_FOUND + itemId);
        }
        Item updatedItem = items.get(itemId);
        if (!updatedItem.getOwner().equals(item.getOwner())) {
            throw new NotOwnerException(USER_NOT_OWNER +
                    "userId: " + item.getOwner() + ", itemId: " + itemId);
        }
        changeItem(updatedItem, item);
        return updatedItem;
    }

    @Override
    public Item getItemById(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ItemNotFoundException(ITEM_NOT_FOUND + itemId);
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) result.add(item);
        }
        return result;
    }

    @Override
    public List<Item> getItemsByRequest(String text) {
        List<Item> result = new ArrayList<>();
        String searchItem = text.toLowerCase();
        for (Item item :
                items.values()) {
            String itemName = item.getName().toLowerCase();
            String itemDescription = item.getDescription().toLowerCase();
            if ((itemName.contains(searchItem) || itemDescription.contains(searchItem))
                    && item.getAvailable().equals(true)) {
                result.add(item);
            }
        }
        return result;
    }

    private long generateId() {
        return currentId++;
    }

    private void changeItem(Item oldItem, Item newItem) {
        String name = newItem.getName();
        if (name != null) {
            oldItem.setName(name);
        }
        String description = newItem.getDescription();
        if (description != null) {
            oldItem.setDescription(description);
        }
        Boolean available = newItem.getAvailable();
        if (available != null) {
            oldItem.setAvailable(available);
        }
    }
}
