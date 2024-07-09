package ru.practicum.shareit.item;


import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Data
public class MemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    public void create(Item item) {
        items.put(item.getId(), item);
    }

    public void update(Item item) {
        items.put(item.getId(), item);
    }

    public Optional<Item> get(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Collection<Item> findAll() {
        return items.values();
    }

    public Collection<Item> findAll(Long userId) {
        return items.values().stream().filter(item -> item.getOwnerId().equals(userId)).toList();
    }

    public Collection<Item> findAll(String text) {
        return items.values().stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }
 }
