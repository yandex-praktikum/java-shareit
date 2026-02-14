package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private long idCounter = 1;

    public Item create(Item item) {
        long id = idCounter++;
        item.setId(id);
        items.put(id, item);
        return item;
    }

    public Item update(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Вещь не найдена");
        }

        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<Item> findByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner() != null && item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchText = text.toLowerCase().trim();

        return items.values().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item -> containsText(item, searchText))
                .collect(Collectors.toList());
    }

    private boolean containsText(Item item, String text) {
        return (item.getName() != null && item.getName().toLowerCase().contains(text)) ||
                (item.getDescription() != null && item.getDescription().toLowerCase().contains(text));
    }
}
