package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("InMemoryItemStorage")
public class InMemoryItemStorage implements ItemStorage{
    ItemMapper itemMapper = new ItemMapper();

    Map<Long, Item> items = new HashMap<>();

    private Long id = 1L;

    @Override
    public ItemDto addItem(Item item) {
        item.setId(id);
        items.put(id, item);
        id++;
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long itemId, Item item) {
        Item updateItem = items.get(itemId);
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getRequest() != null) {
            updateItem.setRequest(item.getRequest());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        items.put(item.getId(), updateItem);
        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        List <ItemDto> itemList= new ArrayList<>();
        for (Item item : items.values().stream().filter(t -> Objects.equals(t.getOwner(), userId)).collect(Collectors.toSet())) {
            itemList.add(itemMapper.toItemDto(item));
        }
        return itemList;
    }

    @Override
    public Item getItemForStorage (Long userId){
        return items.get(userId);
    }

    @Override
    public ItemDto getItem(Long id) {
        return itemMapper.toItemDto(items.get(id));
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }

    public List<ItemDto> searchItem(String text) {
        text = text.toLowerCase();
        Set<ItemDto> item = new HashSet<>();
        for (Item value : items.values()) {
            if (value.getName().toLowerCase().contains(text) && value.getAvailable()) {
                item.add(itemMapper.toItemDto(value));
                continue;
            }
            if (value.getDescription().toLowerCase().contains(text) && value.getAvailable()) {
                item.add(itemMapper.toItemDto(value));
            }
        }
        return item.stream().sorted(Comparator.comparingLong(ItemDto::getId)).distinct().collect(Collectors.toList());
    }


}
