package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Начало обработки запроса на получение всех вещей пользователя с id={}", userId);
        List<ItemDto> itemsDto = itemService.getUserItems(userId);
        log.info("Завершение обработки запроса на получение всех вещей пользователя с id={}", userId);
        return itemsDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable @Positive Long itemId) {
        log.info("Начало обработки запроса на получение вещи с id={}", itemId);
        ItemDto itemDto = itemService.getItem(itemId);
        log.info("Завершение обработки запроса на получение вещи с id={}", itemId);
        return itemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Начало обработки запроса на поиск вещей по запросу: {}", text);
        List<ItemDto> itemsDto = itemService.searchItems(text);
        log.info("Завершение обработки запроса на поиск вещей по запросу: {}", text);
        return itemsDto;
    }

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId) {
        log.info("Начало обработки запроса на создание вещи: {} пользователем с id={}", itemDto, ownerId);
        ItemDto createdItemDto = itemService.createItem(itemDto, ownerId);
        log.info("Завершение обработки запроса на создание вещи: {} пользователем с id={}", itemDto, ownerId);
        return createdItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto updatedItemDto, @PathVariable @Positive Long itemId,
                              @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        updatedItemDto.setId(itemId);
        log.info("Начало обработки запроса на обновление вещи пользователем с id={}: {}", userId, updatedItemDto);
        ItemDto itemDto = itemService.updateItem(updatedItemDto, userId);
        log.info("Завершение обработки запроса на обновление вещи пользователем с id={}: {}", userId, updatedItemDto);
        return itemDto;
    }
}
