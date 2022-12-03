package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemDao implements ItemDao {
    private final Map<Long, Item> itemDao;
    private long idGenerator = 0;

    @Override
    public Item getItemById(Long itemId) {
        return itemDao.get(itemId);
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return itemDao.values().stream()
                      .filter(t -> t.getUserId().equals(userId))
                      .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item) {
        item.setId(++idGenerator);
        itemDao.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        Item itemForUpdate = itemDao.get(itemId);
        if (item.getName() != null) {
            itemForUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemForUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemForUpdate.setAvailable(item.getAvailable());
        }
        return itemForUpdate;
    }

    @Override
    public List<Item> searchItem(String searchText) {
        String regExp = ".*" + searchText.toLowerCase() + ".*";
        if (searchText.equals("")) {
            return Collections.emptyList();
        } else {
            return itemDao.values().stream()
                          .filter(t -> (t.getName().toLowerCase().matches(regExp) ||
                                  t.getDescription().toLowerCase().matches(regExp)) && t.getAvailable())
                          .collect(Collectors.toList());
        }
    }
}
