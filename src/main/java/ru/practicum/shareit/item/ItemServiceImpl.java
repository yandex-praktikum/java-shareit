package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private final UserService userService;

    @Autowired
    public ItemServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final Map<Long, Item> items = new HashMap<>();

    //создать вещь
    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userService.findUserById(ownerId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);

        item.setId(getNextId());
        items.put(item.getId(), item);

        log.info("Создана вещь: {} пользователем: {}", item.getName(), ownerId);

        return ItemMapper.toItemDto(item);
    }

    //вывод всех вещей
    @Override
    public Collection<ItemDto> getAllItems() {
        Collection<ItemDto> itemDtos = new ArrayList<>();

        for (Item item : items.values()) {
            itemDtos.add(ItemMapper.toItemDto(item));
        }
        return itemDtos;
    }

    // информация о вещи по id
    @Override
    public ItemDto getItemById(Long itemId) {

        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        return ItemMapper.toItemDto(item);
    }

    // Только для владельца
    @Override
    public Collection<ItemDto> getItemsByOwner(Long ownerId) {

        List<ItemDto> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId().equals(ownerId)) {
                result.add(ItemMapper.toItemDto(item));
            }
        }
        return result;
    }

    public Collection<ItemDto> searchAvailableItems(String text) {
        List<ItemDto> result = new ArrayList<>();
        String searchText = text.toLowerCase();

        for (Item item : items.values()) {
            if (item.isAvailable() && (item.getName().toLowerCase().contains(searchText) ||
                    item.getDescription().toLowerCase().contains(searchText))) {
                result.add(ItemMapper.toItemDto(item));
            }
        }
        return result;
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto updates, Long userId) {

        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }


        Item existingItem = items.get(itemId);

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Нельзя редактировать чужую вещь");
        }

        boolean wasUpdated = false;

        if (updates.getName() != null && !existingItem.getName().equals(updates.getName())) {
            existingItem.setName(updates.getName());
            log.info("Название вещи изменено на: {}", updates.getName());
            wasUpdated = true;
        }

        if (updates.getDescription() != null && !existingItem.getDescription().equals(updates.getDescription())) {
            existingItem.setDescription(updates.getDescription());
            log.info("Описание вещи изменено на: {}", updates.getDescription());
            wasUpdated = true;
        }

        if (updates.getAvailable() != null && existingItem.isAvailable() != updates.getAvailable()) {
            existingItem.setAvailable(updates.getAvailable());
            log.info("Статус доступности изменен на: {}", updates.getAvailable());
            wasUpdated = true;
        }

        if (!wasUpdated) {
            log.warn("Данные вещи не изменились");
            throw new ValidationException("Данные вещи не изменились");
        }

        return ItemMapper.toItemDto(existingItem);
    }


    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
