package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long id) {
        log.info("Начало сохранение вещи {} пользователя id={}", itemDto, id);
        return itemClient.create(id, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получения всех вещей пользователя с id= {}", id);
        return itemClient.getAll(id, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable @NotNull Long id,
                                      @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Получение вещи с id= {}", id);
        return itemClient.get(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String value,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Поиск вещей по значению= {}", value);
        return itemClient.search(value, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Validated(ItemUpdateMarker.class) @RequestBody ItemDto itemDto,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                         @PathVariable @NotNull Long itemId) {
        log.info("Начало обновление вещи {} c ид= {}, пользователя с id= {}", itemDto, itemId, id);
        return itemClient.update(itemId, id, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                         @PathVariable @NotNull Long itemId) {
        log.info("Начало удаление вещи  c ид= {}, пользователя с id= {}", itemId, id);
        return itemClient.delete(itemId, id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                             @PathVariable @NotNull Long itemId) {
        log.info("Начало добавление комментария {} пользователем id={}", commentDto, id);
        return itemClient.addComment(itemId, id, commentDto);
    }
}
