package ru.practicum.shareit.item;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    //создание вещи
    @PostMapping
    public ItemDto createItemDto(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItemDto itemDto) {
        log.info("POST/items - Запрос на создание вещи пользователем {}: {}", ownerId, itemDto);
        return itemService.createItem(ownerId, itemDto);
    }


    //просмотр вещи по id
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@Positive @PathVariable Long itemId) {
        log.info("GET/items/{} - Запрос на просмотр информации вещи с id: {}", itemId, itemId);
        return itemService.getItemById(itemId);  // любой пользователь
    }

    //просмотр вещй владельца
    @GetMapping()
    public Collection<ItemDto> getMyItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("GET/items - Запрос на вывод вещей владельца: {}", ownerId);
        return itemService.getItemsByOwner(ownerId);  // только свои вещи
    }

    //поиск вещи по названию или описанию
    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        log.info("GET/items/search - Запрос на поиск вещей по тексту: '{}'", text);

        if (text.isEmpty()) {
            return List.of();
        }
        return itemService.searchAvailableItems(text);
    }

    //обновление вещи пользователем
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Positive
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH/items/{} - Запрос на обновление вещи - {} пользователем {}", itemId, itemId, userId);
        return itemService.updateItem(itemId, itemDto, userId);
    }

    //вывод всех вещей
    @GetMapping("/allTeam")
    public Collection<ItemDto> getAllItems() {
        log.info("GET/items - Запрос на вывод всех вещей");
        return itemService.getAllItems();
    }
}
