package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemInMemoryRepository {

    private final List<Item> listOfAllItems = new ArrayList<>();

    public List<Item> getItems(long userId) {
        log.info("ItemInMemoryRepository: Запрос на получение всех вещей пользователя с ID={}", userId);
        return Optional.of(listOfAllItems.stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList())).get();
    }

    public ItemDto getItem(long itemId) {
        log.info("ItemInMemoryRepository: Запрос на получение данных о вещи с ID={}", itemId);
        for (Item item : listOfAllItems) {
            if (item.getId() == itemId) {
                return ItemMapper.toItemDto(item);
            }
        }
        return null;
    }

    public Item addNewItem(long userId, Item item) {
        log.info("ItemInMemoryRepository: Запрос на добавление новой вещи {}", item);
        item.setOwner(userId);
        item.setId(getNextId());
        listOfAllItems.add(item);
        return item;
    }

    public void deleteItem(long userId, long itemId) {
        log.info("ItemInMemoryRepository: Запрос на удаление вещи с ID={}", itemId);
        listOfAllItems.removeIf(item -> (item.getOwner() == userId) && (item.getId() == itemId));
    }

    public void deleteAllUserItems(long userId) {
        log.info("ItemInMemoryRepository: Запрос на удаление всех вещей пользователя с ID={}", userId);
        listOfAllItems.removeIf(item -> (item.getOwner() == userId));
    }

    public Item changeItem(long userId, long itemId, Item item) {
        log.info("ItemInMemoryRepository: Запрос на обновление вещи с ID={}", itemId);
        for (Item itemToFind : listOfAllItems) {
            if ((itemToFind.getId().equals(itemId)) && (itemToFind.getOwner() == userId)) {
                if (item.getDescription() != null) {
                    itemToFind.setDescription(item.getDescription());
                }
                if (item.getName() != null) {
                    itemToFind.setName(item.getName());
                }
                if (item.getAvailable() != null) {
                    itemToFind.setAvailable(item.getAvailable());
                }
                return itemToFind;
            }
        }
        return null;
    }

    public List<ItemDto> searchItem(String textToSearch) {
        log.info("ItemInMemoryRepository: Запрос на поиск вещи по тексту \"{}\"", textToSearch);
        List<ItemDto> resultItemsInName = new ArrayList<>();
        List<ItemDto> resultItemsInDescription = new ArrayList<>();
        if (listOfAllItems.isEmpty()) {
            return new ArrayList<>();
        }
        for (Item item : listOfAllItems) {
            if (item.getName().toLowerCase().contains(textToSearch.toLowerCase())) {
                if (item.getAvailable()) {
                    resultItemsInName.add(ItemMapper.toItemDto(item));
                }
            }
            if (item.getDescription().toLowerCase().contains(textToSearch.toLowerCase())) {
                if (item.getAvailable()) {
                    resultItemsInDescription.add(ItemMapper.toItemDto(item));
                }
            }
        }
        Set<ItemDto> setOfItems = new HashSet<>();
        setOfItems.addAll(resultItemsInName);
        setOfItems.addAll(resultItemsInDescription);
        return new ArrayList<>(setOfItems);
    }

    private long getNextId() {
        return listOfAllItems.size() + 1;
    }
}
