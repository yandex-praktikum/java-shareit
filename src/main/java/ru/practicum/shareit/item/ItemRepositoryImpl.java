package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
@AllArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> itemMap = new HashMap<>();

    @Override
    public Item create(Long ownerId, Item item) {
        item.setOwner(ownerId);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> findAllItemsOwner(Long ownerId) {
        return itemMap.values()
                .stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        return itemMap.get(item.getId());
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemMap.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()) &&
                                item.getIsAvailable())
                .collect(Collectors.toList());
    }
}
