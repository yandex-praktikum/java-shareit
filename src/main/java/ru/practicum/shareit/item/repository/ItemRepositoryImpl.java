package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;
@Repository
public class ItemRepositoryImpl implements ItemRepository{
    private Map<Long, Item> itemMap = new HashMap<>();
    private long tmpId = 0;
    @Override
    public Item create(Item item) {
        tmpId++;
        item.setId(tmpId);
        itemMap.put(tmpId,item);
        return item;
    }
}
