package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long id) {
        log.info("Начало сохранение вещи {} пользователя id={}", itemDto, id);
        return itemService.create(itemDto, id);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long id,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получения всех вещей пользователя с id= {}", id);
        return itemService.getAll(id, from, size);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение вещи с id= {}", id);
        return itemService.get(id, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String value,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск вещей по значению= {}", value);
        return itemService.search(value, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long id,
                          @PathVariable Long itemId) {
        log.info("Начало обновление вещи {} c ид= {}, пользователя с id= {}", itemDto, itemId, id);
        return itemService.update(itemDto, id, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long id,
                       @PathVariable Long itemId) {
        log.info("Начало удаление вещи  c ид= {}, пользователя с id= {}", itemId, id);
        itemService.delete(id, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long id,
                                 @PathVariable Long itemId) {
        log.info("Начало добавление комментария {} пользователем id={}", commentDto, id);
        return itemService.addComment(commentDto, id, itemId);
    }
}
