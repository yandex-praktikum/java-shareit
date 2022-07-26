package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRepositoryImpl implements ItemRepository{

    private static int id = 0;
    private final HashMap<Integer, Item> itemHashMap = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        if (item.getId() == 0){
            id++;
            item.setId(id);
        }
        itemHashMap.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(int itemId) {
        itemHashMap.remove(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(itemHashMap.values());
    }

    @Override
    public Item getItemById(int itemId) {
        return itemHashMap.get(itemId);
    }

    @Override
    public List<Item> findItemByUserId(int userId) {
        return itemHashMap.values().stream().filter(item -> item.getUserId() == userId).collect(Collectors.toList());
    }

}
