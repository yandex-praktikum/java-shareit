package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, Item> storage = new HashMap<>();

    @Override
    public Item save(Item item) {
        log.info("ItemRepository: save({})", item);
        item.setId(counter.incrementAndGet());
        return storage.computeIfAbsent(item.getId(), oldItem -> item);
    }

    @Override
    public Item update(Item item) {
        log.info("ItemRepository: update({})", item);
        return storage.computeIfPresent(item.getId(), (id, oldI) -> item);
    }

    @Override
    public Item get(int itemId) {
        log.info("ItemRepository: get({})", itemId);
        return storage.get(itemId);
    }

    @Override
    public List<Item> getOwnerItems(int ownerId) {
        log.info("ItemRepository: getOwnerItems({})", ownerId);
        return storage.values().stream()
                .filter(i -> i.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> find(String text) {
        log.info("ItemRepository: find({})", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return storage.values().stream()
                    .filter(i -> i.getAvailable() && (i.getName().toUpperCase().contains(text.toUpperCase())
                            || i.getDescription().toUpperCase().contains(text.toUpperCase())))
                    .collect(Collectors.toList());
        }
    }
}
