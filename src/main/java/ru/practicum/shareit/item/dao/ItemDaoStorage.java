package ru.practicum.shareit.item.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDaoStorage implements ItemDao {
    private final Map<Long, List<Item>> itemsMap = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Item create(Item item) {
        item.setId(itemId);
        itemId += 1;

        itemsMap.compute(item.getOwner().getId(), (userId, userItems) -> {
            if (Objects.isNull(userItems)) {
                userItems = new ArrayList<>();
            }

            userItems.add(item);

            return userItems;
        });

        return item;
    }

    @Override
    public Optional<Item> findByUserIdAndItemId(Long userId, Long itemId) {
        List<Item> items = itemsMap.get(userId);

        if (Objects.nonNull(items)) {
            return items
                    .stream()
                    .filter(item -> Objects.equals(item.getId(), itemId))
                    .findFirst();
        }

        return Optional.empty();
    }

    public Optional<Item> findById(Long itemId) {
        List<Item> items = new ArrayList<>();
        itemsMap.values().forEach(items::addAll);

        return items
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public List<Item> findAllByUserId(Long userId) {
        return itemsMap.get(userId);
    }

    @Override
    public List<Item> findByText(String text) {
        List<Item> items = new ArrayList<>();
        itemsMap.values().forEach(items::addAll);

        if (text.length() > 0) {
            return items
                    .stream()
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public Item update(Item item, Long itemId) {
        List<Item> items = itemsMap.get(item.getOwner().getId());
        int index = items.indexOf(item);

        items.set(index, item);

        return item;
    }
}