package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final UserRepository userRepository;

    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        userRepository.getUser(userId);
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItem(Long itemId) {
        Item item = items.get(itemId);
        if (item == null)
            throw new ObjectNotFoundException("Вещь с id=" + itemId + " не найдена");
        return item;
    }

    @Override
    public Item createItem(Item item) {
        userRepository.getUser(item.getOwnerId());
        idCounter++;
        item.setId(idCounter);
        items.put(idCounter, item);
        return item;
    }

    @Override
    public Item updateItem(Item updatedItem, Long redactorId) {
        userRepository.getUser(redactorId);
        Item item = getItem(updatedItem.getId());
        if (!item.getOwnerId().equals(redactorId))
            throw new NoAccessException("Пользователь с id=" + redactorId + " не является владельцем вещи с id=" +
                    updatedItem.getId());
        if (updatedItem.getName() != null)
            item.setName(updatedItem.getName());
        if (updatedItem.getDescription() != null)
            item.setDescription(updatedItem.getDescription());
        if (updatedItem.getAvailable() != null)
            item.setAvailable(updatedItem.getAvailable());
        items.put(item.getId(), item);
        return item;
    }

    // Согласно тестам, эта фича пока не предусмотрена
//    @Override
//    public void deleteItem(Long itemId) {
//
//    }
}
