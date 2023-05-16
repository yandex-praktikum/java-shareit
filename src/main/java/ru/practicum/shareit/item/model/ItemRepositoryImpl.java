package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public ItemDto addItem(Item item, long ownerId) {
        item.setOwnerId(ownerId);
        item.setId(getId());
        items.put(item.getId(), item);
        return ItemMapper.ItemToDto(item);
    }

    @Override
    public ItemDto updateItem(Item item, long ownerId, long itemId) {
        if (items.containsKey(itemId) && items.get(itemId).getOwnerId() == ownerId) {
            Item oldItem = items.get(itemId);
            if (item.getDescription() != null) {
                oldItem.setDescription(item.getDescription());
            }
            if (item.getName() != null) {
                oldItem.setName(item.getName());
            }
            if (item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
            }
            return ItemMapper.ItemToDto(oldItem);
        }
        throw new NoSuchItemException(String.format("Unable to update Item. " +
                "There is no Item with ID=%s.", item.getId()));
    }

    @Override
    public ItemDto getItem(long itemId) {
        try {
            return ItemMapper.ItemToDto(items.get(itemId));
        } catch (NullPointerException e) {
            throw new NoSuchItemException(String.format("There is no Item with ID=%s.", itemId));
        }
    }

    @Override
    public List<ItemDto> getUsersOwnItems(long ownerId) {
        return items.values()
                .stream()
                .filter(value -> value.getOwnerId() == ownerId)
                .map(ItemMapper::ItemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemByDescription(String searchText) {
        if (searchText.isBlank()) {
            return new ArrayList<>();
        }
        HashSet<String> keyWords = Stream.of(searchText.replaceAll("\\s{2,}", " ")
                .replaceAll("//W{1,}", " ")
                .trim()
                .toLowerCase()
                .split(" ")).filter(kw -> kw.length() >= 4).collect(Collectors.toCollection(HashSet::new));

        List<ItemDto> searchResult = new ArrayList<>();
        for (Item item : items.values().stream().filter(Item::getAvailable).collect(Collectors.toList())) {
            if (keyWords.stream()
                    .allMatch(kw -> item.getName().toLowerCase().contains(kw) ||
                            item.getDescription().toLowerCase().contains(kw))) {
                searchResult.add(ItemMapper.ItemToDto(item));
            }
        }

        if (searchResult.isEmpty()) {
            return new ArrayList<>();
        } else {
            return searchResult;
        }
    }

    private long getId() {
        long lastId = items.values()
                .stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}