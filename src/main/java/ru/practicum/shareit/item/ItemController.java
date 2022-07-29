package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с вещами
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Возвращает список всех вещей пользователя userId
     *
     * @param userId объекта пользователя
     * @return списка объектов вещей
     */
    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    /**
     * Возвращает вещь по itemId независимо от userId пользователя
     *
     * @param itemId объекта вещи
     * @return объект вещи
     */
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    /**
     * Возвращает список по введенному тексту поиска text
     *
     * @param text поиска
     * @return список объектов вещей
     */
    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        return itemService.searchItemByText(text);
    }

    /**
     * Создаёт объект вещи пользователя userId
     *
     * @param userId объекта пользователя
     * @return возвращает объект вещи, который был создан
     */
    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    /**
     * Обновляет данные объекта вещи itemId пользователя userId
     *
     * @param itemId объекта вещи
     * @param userId объекта пользователя
     * @return возвращает обновленный объект вещи
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    /**
     * Удаляет объект вещи itemId пользователя userId
     *
     * @param itemId объекта вещи
     * @param userId объекта пользователя
     */
    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}
