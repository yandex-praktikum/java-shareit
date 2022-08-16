package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemStorage implements ItemRepository {
    private final HashMap<Long, List<Item>> items = new HashMap<>();
    private long id;

    @Override
    public List<ItemDto> findByUserId(long userId) {
        return items.get(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getListItems() {
        return items.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .stream()
                .map(ItemMapper::toItemDto)
                .findAny().orElse(null);
    }

    @Override
    public ItemDto save(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(userId, itemDto);
        List<Item> itemList = new ArrayList<>();
        if(items.containsKey(userId)) {
            itemList = items.get(userId);
        }
        item.setId(++id);
        item.setOwner(userId);
        itemList.add(item);
        items.put(userId, itemList);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        for (Item item : items.get(userId)) {
            if (item.getId() == itemId) {
                items.remove(item);
                log.info("Вещь ID# {} удалена", itemId);
            }
        }
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws NotFoundException {
        if (!items.containsKey(userId)) {
            log.info("У пользователя нет вещей");
            throw new NotFoundException("У пользователя нет вещей");
        }
        Item item = ItemMapper.toItem(userId, itemDto);
        Item newItem = null;
        for (Item i : items.get(userId)) {
            if (i.getId() == itemId) {
                if (item.getName() != null) {
                    i.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    i.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    i.setAvailable(item.getAvailable());
                }
                newItem = i;
                break;
            }
        }
        if(newItem == null){
            log.info("Вещь не найдена");
            throw new NotFoundException("Вещь не найдена");
        }
        log.info("Вещь ID# {} обновлена", itemId);
        return ItemMapper.toItemDto(newItem);
    }

    public List<ItemDto> searchItem(String search) {
        List<ItemDto> resultSearchItem = new ArrayList<>();
        for (Item item : getListItems()) {
            if (item.getName().toUpperCase().contains(search.toUpperCase()) ||
                    item.getDescription().toUpperCase().contains(search.toUpperCase())) {
                if (item.getAvailable()) {
                    resultSearchItem.add(ItemMapper.toItemDto(item));
                }
            }
        }
        return resultSearchItem;
    }
}