package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemStorageImpl implements ItemStorage {
    private Long counter = 1L;
    private final Map<Long, Item> items = new HashMap<>();
    @Override
    public Item get(Long id) {
        return null;
    }

    @Override
    public Collection<Item> getAllByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public Item add(Item item) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public Collection<Item> search(String keyword, Long userId) {
        return null;
    }
}
