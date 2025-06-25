package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepositoryInMemory implements ItemRepository {

    private final Map<Long, Item> itemMap = new HashMap<>();

    @Override
    public Item save(Item item) {
        if (item.getId() != null) {
            log.warn("нельзя создать пользователя с id " + item.getId());
            throw new ValidationException("нельзя создать вещь с id " + item.getId());
        }
        item.setId(getNextId());
        itemMap.put(item.getId(), item);
        log.info("создана вещь c id " + item.getId());
        return item;
    }

    @Override
    public void delete(long id) {
        log.info("вещь удалена id " + id);
        itemMap.remove(id);
    }

    @Override
    public Item update(Item item) {
        Item oldItem = itemMap.get(item.getId());
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        log.info("вещь обновлена id " + oldItem.getId());
        return oldItem;
    }

    @Override
    public Item getByItemId(long id) {
        log.info("запрос вещи id " + id);
        return itemMap.get(id);
    }

    @Override
    public Collection<Item> getAllByUserId(long id) {
        log.info("запрос списка вещей пользователя id " + id);
        return itemMap.values().stream()
                .filter(item -> item.getOwner() == id)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> findItemByName(String name) {
        log.info("запрос списка вещей по имени " + name);
        return itemMap.values().stream()
                .filter(item -> name.equals(item.getName().toLowerCase()) || name.equals(item.getDescription().toLowerCase()))
                .filter(item -> item.getAvailable())
                .collect(Collectors.toList());
    }

    private long getNextId() {
        long currentMaxId = itemMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
