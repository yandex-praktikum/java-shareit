package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс, ответственный за хранение объектов вещей в памяти
 */
@Component
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage {
    private long itemIdGenerated;
    private final List<ItemDto> items = new ArrayList<>();
    private final Map<Long, List<ItemDto>> userItems = new HashMap<>();

    /**
     * Возвращает список всех вещей пользователя
     */
    @Override
    public List<ItemDto> getItems(long userId) {
        return userItems.get(userId);
    }

    /**
     * Возвращает вещь по ID
     */
    @Override
    public ItemDto getItemById(long itemId) {
        return userItems.values().stream()
                .flatMap(list -> list.stream())
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена",
                        itemId)));
    }

    /**
     * Добавляет вещь
     */
    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        if (!userItems.containsKey(userId)) {
            userItems.put(userId, new ArrayList<>());
        }
        itemDto.setId(generateId());
        userItems.get(userId).add(itemDto);

        return itemDto;
    }

    /**
     * Валидирует поля объекта и обновляет вещь в памяти
     */
    @Override
    public ItemDto update(long userId, ItemDto itemDtoExisting, ItemDto itemDto) {
        if (!(itemDto.getAvailable() == null)) {
            itemDtoExisting.setAvailable(itemDto.getAvailable());
        }
        if (!(itemDto.getName() == null)) {
            itemDtoExisting.setName(itemDto.getName());
        }
        if (!(itemDto.getDescription() == null)) {
            itemDtoExisting.setDescription(itemDto.getDescription());
        }
        return itemDtoExisting;
    }

    /**
     * Удаление вещи из памяти
     */
    @Override
    public void delete(long userId, long itemId) {
        for (ItemDto itemDtoExisting : userItems.get(userId)) {
            if (itemDtoExisting.getId() == itemId) {
                items.remove(itemDtoExisting);
                return;
            }
        }
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> foundItems = new ArrayList<>();

        if (!text.isBlank()) {
            foundItems = userItems.values().stream()
                    .flatMap(list -> list.stream())
                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                            && item.getAvailable().equals("true"))
                    .collect(Collectors.toList());
        }
        return foundItems;
    }

    /**
     * Возвращает вещь пользователя по ID
     */
    @Override
    public ItemDto getItemByIdAndUserId(long userId, long itemId) {
        if (!userItems.containsKey(userId)) {
            throw new NotFoundException(String.format("У пользователя с id %d нет вещей",
                    userId));
        }

        return userItems.get(userId).stream()
                .filter(p -> p.getId() == (itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена",
                        itemId)));
    }

    private long generateId() {
        return ++itemIdGenerated;
    }
}
