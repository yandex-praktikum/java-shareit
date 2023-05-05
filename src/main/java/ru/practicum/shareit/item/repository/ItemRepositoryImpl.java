package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;

import java.util.*;

@Slf4j
@Component
public class ItemRepositoryImpl implements ItemRepository {

    private Integer itemId = 0;

    private final Map<Integer, Item> items = new HashMap<>();

    private final Map<String, Integer> itemsForSearch = new LinkedHashMap<>();

    @Override
    public List<Item> getAllItems(int userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId() == userId) {
                userItems.add(item);
            }
        }
        if (!userItems.isEmpty()) {
            return userItems;
        } else {
            log.info("Пользователь не найден или у него нет вещей");
            throw new UserNotFoundException();
        }
    }

    @Override
    public ItemDto add(int userId, Item item) {
        setItemId(getItemId() + 1);
        item.setId(getItemId());
        itemsForSearch.put(item.getName() + item.getDescription() + item.getOwner().getId(), item.getId());
        items.put(item.getId(), item);
        log.info("Вещь создана");
        return ItemMapper.itemToItemDto(item, new ArrayList<>());
    }

    @Override
    public ItemDto update(int userId, int id, ItemDto itemDto) {
        Item updatedItem = null;
        if (items.containsKey(id)) {
            Item item = items.get(id);
            if (item.getOwner().getId() == userId) {
                if ((itemDto.getName() == null) && (itemDto.getDescription() == null)) {
                    updatedItem = updateStatus(itemDto, item);
                } else if (itemDto.getDescription() == null) {
                    updatedItem = updateName(itemDto, item);
                } else if (itemDto.getName() == null) {
                    updatedItem = updateDescription(itemDto, item);
                } else {
                    updatedItem = fullUpdateItem(itemDto, item);
                }
                if (updatedItem != null) {
                    items.remove(id);
                    itemsForSearch.remove(item.getName() + item.getDescription() + item.getOwner().getId());
                    itemsForSearch.put(updatedItem.getName() + updatedItem.getDescription() +
                            updatedItem.getOwner().getId(), updatedItem.getId());
                    items.put(id, updatedItem);
                    log.info("Вещь обновлена");
                }
            } else {
                log.info("Некорректный запрос - у вещи другой владелец");
                throw new ItemNotFoundException();
            }
        } else {
            log.info("Вещь не найдена");
            throw new ItemNotFoundException();
        }
        return ItemMapper.itemToItemDto(updatedItem, new ArrayList<>());
    }

    @Override
    public Optional<ItemDto> findById(int userId, int id) {
        if (items.containsKey(id)) {
            return Optional.of(ItemMapper.itemToItemDto(items.get(id), new ArrayList<>()));
        } else {
            log.info("Вещь с id = " + id + " не найдена");
            return Optional.empty();
        }
    }

    @Override
    public void delete(int userId, int id) {
        if (items.containsKey(id)) {
            Item item = items.get(id);
            items.remove(id);
            itemsForSearch.remove(item.getName() + item.getDescription() + item.getOwner().getId());
            log.info("Вещь с id = " + id + " удалена");
        } else {
            log.info("Вещь с id = " + id + " не найдена");
            throw new ItemNotFoundException();
        }
    }

    @Override
    public List<ItemDto> findItems(String searchRequest) {
        List<ItemDto> searchedItems = new ArrayList<>();
        for (String itemDescription : itemsForSearch.keySet()) {
            if (itemDescription.toLowerCase().contains(searchRequest.toLowerCase())) {
                Item item = items.get(itemsForSearch.get(itemDescription));
                if (item.getAvailable()) {
                    searchedItems.add(ItemMapper.itemToItemDto(item, new ArrayList<>()));
                }
            }
        }
        return searchedItems;
    }

    private Item updateName(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(itemDto.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    private Item updateDescription(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(itemDto.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    private Item updateStatus(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(itemDto.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    private Item fullUpdateItem(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

}
