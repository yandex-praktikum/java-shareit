package ru.practicum.shareit.item.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {
    private final Map<Integer, Item> itemMap = new HashMap<>();
    @Autowired
    private ItemMapper itemMapper;
    private int count = 0;

    @Override
    public ItemDto addItem(int userId, Item item) {
        item.setOwnerId(userId);
        item.setId(++count);
        ItemDto itemDto = itemMapper.itemDto(item);
        itemMap.put(item.getId(), item);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields) {
        Item item = getItemById(itemId);
        if (item.getOwnerId() == userId) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Item.class, (String) key);
                field.setAccessible(true);
                if (!((String) key).equalsIgnoreCase("id")
                        && !((String) key).equalsIgnoreCase("ownerId")) {
                    ReflectionUtils.setField(field, item, value);
                }
            });
            return itemMapper.itemDto(item);
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId
                    + " не является владельцем данной вещи. Изменения не сохранены.");
        }
    }

    @Override
    public Item getItemById(int itemId) {
        if (itemMap.containsKey(itemId)) {
            return itemMap.get(itemId);
        } else {
            throw new NoDataFoundException("Item с id: " + itemId + " не найдена.");
        }
    }

    @Override
    public ItemDto getItemDtoById(int itemId) {
        if (itemMap.containsKey(itemId)) {
            return itemMapper.itemDto(itemMap.get(itemId));
        } else {
            throw new NoDataFoundException("Item с id: " + itemId + " не найдена.");
        }
    }

    @Override
    public List<ItemDto> getAllItemForOwner(int ownerId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getOwnerId() == ownerId) {
                itemDtoList.add(itemMapper.itemDto(item));
            }
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItem(String request) {
        if (request.isBlank()) {
            return new ArrayList<>();
        }
        List<ItemDto> listItem = new ArrayList<>();
        Pattern pattern = Pattern.compile(request.toLowerCase());
        for (Item item : itemMap.values().stream().filter(item -> item.getAvailable() == true)
                .collect(Collectors.toList())) {
            Matcher matcher = pattern.matcher((item.getName() + " " + item.getDescription()).toLowerCase());
            if (matcher.find()) {
                listItem.add(itemMapper.itemDto(item));
            }
        }
        return listItem;
    }
}
