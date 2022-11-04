package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component("itemInMemoryDao")
public class ItemInMemoryDao implements ItemDaoStorage {
    private Map<Long, List<Item>> userItems;

    @Override
    public Item addToUserItemsList(Long userId, Item item) {
        if (!userItems.containsKey(userId)) {
            userItems.put(userId, new ArrayList<>());
        }
        userItems.get(userId).add(item);
        return getItemById(userId, item.getId()).orElseThrow(
                () -> new RuntimeException("Ошибка при добавлении айтема"));
    }

    @Override
    public Optional<Item> update(Long userId, Long itemId, Item itemFromRequest) {
        for (Long userIds : userItems.keySet()) {
            if ((userIds.equals(userId))) {
                for (Item item : userItems.get(userId)) {
                    if (item.getId().equals(itemId)) {
                        itemBuild(item, itemFromRequest);
                        return getItemById(userId, itemId);
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> getItemById(Long userId, Long itemId) {
        for (Long userIds : userItems.keySet()) {
            if (userIds.equals(userId)) {
                return userItems.get(userId).stream()
                        .filter(i -> i.getId().equals(itemId))
                        .findFirst();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> getItemByIdForAllUser(Long itemId) {
        return userItems.values().stream()
                .flatMap(Collection::stream)
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public List<Item> findAll(Long userId) {
        for (Long userIds : userItems.keySet()) {
            if (userIds.equals(userId)) {
                return new ArrayList<>(userItems.get(userIds));
            }
        }
        throw new IllegalArgumentException("Юзера с id " + userId + " пока в каталоге нет");
    }

    @Override
    public List<Item> findItemByText(String text) {
        return userItems.values().stream()
                .flatMap(Collection::stream)
                .filter(i -> i.getName().trim().toLowerCase().contains(text.toLowerCase())
                 || i.getDescription().trim().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private void itemBuild(Item itemForUpdate, Item itemFromRequest) {
        if (itemFromRequest.getName() != null) {
            itemForUpdate.setName(itemFromRequest.getName());
        }
        if (itemFromRequest.getDescription() != null) {
            itemForUpdate.setDescription(itemFromRequest.getDescription());
        }
        if (itemFromRequest.getAvailable() != null) {
            itemForUpdate.setAvailable(itemFromRequest.getAvailable());
        }
    }
}
