package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Класс, ответственный за операции с вещами
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(UserService userService, ItemStorage itemStorage) {
        this.userService = userService;
        this.itemStorage = itemStorage;
    }

    /**
     * Возвращает список всех вещей пользователя
     */
    @Override
    public List<ItemDto> getItems(long userId) {
        return itemStorage.getItems(userId);
    }

    /**
     * Возвращает вещь по ID
     */
    @Override
    public ItemDto getItemById(long userId, long itemId) {
        validationId(itemId);

        return itemStorage.getItemById(itemId);
    }

    /**
     * Добавляет вещь
     */
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        userService.getUserById(userId);

        return itemStorage.add(userId, itemDto);
    }

    /**
     * Валидирует поля объекта и обновляет объект вещи
     */
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        ItemDto itemDtoExisting = itemStorage.getItemByIdAndUserId(userId, itemId);

        return itemStorage.update(userId, itemDtoExisting, itemDto);
    }

    /**
     * Удаление вещи
     */
    @Override
    public void deleteItem(long userId, long itemId) {
        validationId(userId);
        validationId(itemId);

        itemStorage.delete(userId, itemId);
    }

    /**
     * Поиск вещи по тексту
     */
    @Override
    public List<ItemDto> searchItemByText(String text) {
        return itemStorage.search(text);
    }

    private void validationId(long id) {
        try {
            if (id <= 0) {
                throw new ValidationException("id должен быть больше 0");
            }
        } catch (ValidationException e) {
            throw e;
        }
    }
}