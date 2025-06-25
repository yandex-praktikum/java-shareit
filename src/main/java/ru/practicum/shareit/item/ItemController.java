package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        log.info("запрос создания вещи");
        return itemService.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId,
                           @RequestBody ItemDto itemDto) {
        log.info("запрос обновление вещи");
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void delItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId) {
        log.info("запрос удаления вещи");
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item getItemById(@PathVariable("itemId") long itemId) {
        log.info("запрос вещи по id " + itemId);
        return itemService.getItemByItemId(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Item> getItemByUserID(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("запрос вещей у пользователя id " + userId);

        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Item> searchItemByName(@RequestParam String text) {
        log.info("запрос поиска вещи name " + text);
        return itemService.findItemByName(text);

    }

}
