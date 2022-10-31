package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ItemRepositoryImpl implements ItemRepository{
    private Map<Long, Item> itemMap = new HashMap<>();
    private Long tmpId = 0L;
    @Override
    public Item create(Item item) {
        tmpId++;
        item.setId(tmpId);
        itemMap.put(tmpId,item);
        return item;
    }
    @Override
    public void update(Item item) {
        itemMap.replace(item.getId(), item);
    }
    @Override
    public Item getById(Long itemID){
        return itemMap.get(itemID);
    }
    @Override
    public List<Item> getAll(Long userId) {
        ArrayList<Item> userItems = new ArrayList<>();
        for (Item item : itemMap.values()){
            if (item.getOwner().equals(userId)){
                userItems.add(item);
            }
        }
        return userItems;
    }

    public List<Item> searchItem(String request) {
        ArrayList<Item> items = new ArrayList<>();
        if (!request.isBlank()){
            for (Item item : itemMap.values()) {
                if (item.getAvailable()&&
                        item.getDescription().toLowerCase().contains(request.toLowerCase()) ||
                        item.getName().toLowerCase().contains(request.toLowerCase())) {
                    items.add(item);
                }
            }
        }

        return items;
    }
}
