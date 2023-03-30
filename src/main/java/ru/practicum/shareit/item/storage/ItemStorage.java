package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    public ItemDto addItem(Item item);

    public ItemDto updateItem(Long itemId, Item item);

    public List<ItemDto> getItems(Long userId);

    public ItemDto getItem(Long id);

    public void deleteItem(Long id);

    public List<ItemDto> searchItem(String text);

    public Item getItemForStorage (Long id);

}
