package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImpl implements ItemStorage {
    private Long counter = 1L;
    private final Map<Long, Item> items = new HashMap<>();
    @Override
    public Item get(Long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Вещь с id: " + id + " не зарегистрирована!");
        }
        return items.get(id);
    }

    @Override
    public Collection<Item> getAllByOwnerId(Long ownerId) {
        return items.values()
                .stream()
                .filter(item -> Objects.equals(item.getOwner(), ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item add(Item item) {
        if (item.getAvailable() == null) {
            throw new NotValidException("Поле Доступность не может быть пустым!");
        }
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new NotValidException("Поле Название не может быть пустым!");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new NotValidException("Поле Описание не может быть пустым!");
        }
        item.setId(counter++);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Вещь с id: " + item.getId() + " не зарегистрирована!");
        }
        Item updatedItem = items.get(item.getId());
        if (item.getName() != null && !item.getName().isEmpty()) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        items.put(updatedItem.getId(), updatedItem);
        return items.get(updatedItem.getId());
    }

    @Override
    public Boolean delete(Long id) {
        items.remove(id);
        return !items.containsKey(id);
    }

    @Override
    public Collection<Item> search(String keyword, Long userId) {
        Collection<Item> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            result.addAll(items.values().stream()
                    .filter(item -> item.getAvailable() && (item.getName().toLowerCase().contains(keyword.toLowerCase())
                                            || item.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .collect(Collectors.toList()));
        }
        return result;
    }
}
