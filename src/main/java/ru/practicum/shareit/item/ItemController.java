package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @Valid @RequestBody ItemDto itemDto) throws NotFoundException {
        log.debug("Добавлена вещь: {}", itemDto);
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) throws NotFoundException {
        log.debug("Обновлена вещь: {}", itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.debug("Получен запрос вещи по ID");
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Получен запрос cписка вещей");
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        log.debug("Получен запрос вещей по наименованию");
        return itemService.search(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        log.debug("Получен запрос на удаление вещи");
        itemService.delete(userId, itemId);
    }
}