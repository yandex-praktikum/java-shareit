package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryInMemory implements ItemRepository {
    private static int generatorId = 0;
    private final List<Item> items = new ArrayList<>();

    @Override
    public Optional<Item> getItemById(long itemId) {
        return items.stream().filter(item -> item.getId() == itemId).findFirst();
    }

    @Override
    public List<Item> getItemsByOwner(long userId) {
        return items.stream().filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemBySearch(String text) {
        return items.stream()
                .filter(item -> item.getAvailable() && ((item.getName().toLowerCase().contains(text.toLowerCase())) ||
                        (item.getDescription().toLowerCase().contains(text.toLowerCase()))))
                .collect(Collectors.toList());
    }

    @Override
    public Item saveNewItem(Item item, long userId) {
        item.setId(++generatorId);
        item.setOwnerId(userId);
        items.add(item);
        return item;
    }

}
