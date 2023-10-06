package ru.practicum.shareit.storage.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Integer, Item> itemHashMap = new HashMap<>();
    private Integer generatedItemId = 1;

    @Override
    public Item addItem(Item item) {
        item.setId(generatedItemId++);
        itemHashMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (itemHashMap.containsKey(item.getId())) {
            Item itemToUpdate = itemHashMap.get(item.getId());

            if (item.getName() != null && !item.getName().equals(itemToUpdate.getName())) {
                itemToUpdate.setName(item.getName());
            }
            if (item.getDescription() != null && !item.getDescription().equals(itemToUpdate.getDescription())) {
                itemToUpdate.setDescription(item.getDescription());
            }
            if (item.getName() != null && !item.getName().equals(itemToUpdate.getName())) {
                itemToUpdate.setName(item.getName());
            }
            if (item.getAvailable() != null && item.getAvailable() && !itemToUpdate.getAvailable()) {
                itemToUpdate.setAvailable(true);
            }
            if (item.getAvailable() != null && !item.getAvailable() && itemToUpdate.getAvailable()) {
                itemToUpdate.setAvailable(false);
            }
            if (item.getOwner() != null) {
                if (item.getOwner().getId() != itemToUpdate.getOwner().getId()) {
                    itemToUpdate.setOwner(item.getOwner());
                }
            }
            itemHashMap.put(item.getId(), itemToUpdate);
        } else {
            return null;
        }
        return item;
    }

    @Override
    public Item getItemById(int id) {
        if (itemHashMap.get(id) == null) {
            return null;
        } else {
            return itemHashMap.get(id);
        }
    }

    @Override
    public List<Item> getAllItemsByOwnerId(int id) {
        Collection<Item> values = itemHashMap.values();

        List<Item> ownerItems = values.stream()
                .filter(item -> item.getOwner().getId() == id)
                .collect(Collectors.toList());
        return ownerItems;
    }

    @Override
    public List<Item> searchItem(String keyWord) {
        if (keyWord.isBlank() || keyWord == null) {
            return new ArrayList<Item>();
        } else {
            Collection<Item> values = itemHashMap.values();

            List<Item> allAvailableItems = values.stream()
                    .filter(item -> item.getAvailable() == true)
                    .collect(Collectors.toList());

            List<Item> filteredItems = allAvailableItems.stream()
                    .filter(item -> containsText(item, keyWord))
                    .collect(Collectors.toList());

            return filteredItems;
        }
    }

    private static boolean containsText(Item item, String text) {
        text = text.toLowerCase();
        StringBuilder combinedText = new StringBuilder();
        combinedText.append(item.getName()).append(item.getDescription());
        return combinedText.toString().toLowerCase().contains(text);
    }
}
