package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @Valid @RequestBody Item item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        return itemService.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("{itemId}")
    public Item getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<Item> getUsersItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<Item> getSearchedItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam String text) {
        return itemService.getSearchedItems(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}
