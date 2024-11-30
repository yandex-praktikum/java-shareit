package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemController: Выполнение запроса на получение вещей пользователя c ID={}", userId);
        return itemService.getItems(userId);
    }

    @GetMapping("{itemId}")
    public ItemDto getItem(@PathVariable(name = "itemId") long itemId) {
        log.info("ItemController: Выполнение запроса на получение вещи c ID={}", itemId);
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("ItemController: Выполнение запроса поиск текста \"{}\"", text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.searchItem(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody ItemDto item) {
        log.info("ItemController: Выполнение запроса добавление вещи {}", item);
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDto changeItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable(name = "itemId") long itemId,
                              @RequestBody ItemDto item) {
        log.info("ItemController: Выполнение запроса обновление вещи с id={} на вещь: {}", itemId, item);
        return itemService.changeItem(userId, itemId, item);
    }

    @DeleteMapping("{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId) {
        log.info("ItemController: Выполнение запроса удаление вещи с ID={}", itemId);
        itemService.deleteItem(userId, itemId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("Ошибка", e.getMessage());
    }
}
