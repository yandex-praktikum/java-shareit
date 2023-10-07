package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.item.ItemServiceInterface;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class ItemController {
    @Autowired
    private final ItemServiceInterface itemService;
    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/items")
    public ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int sharerUserId) {
        return itemService.addItem(itemDto, sharerUserId);
    }

    @PatchMapping("/items/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int sharerUserId,
                              @PathVariable int itemId) {
        return itemService.updateItem(itemDto, sharerUserId, itemId);
    }

    @GetMapping("items/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/items")
    public List<Item> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") int sharerUserId) {
        return itemService.getAllItemsByOwnerId(sharerUserId);
    }

    @GetMapping("/items/search")
    public List<Item> searchItem(@RequestParam(name = "text") String text) {
        return itemService.searchItem(text);
    }
}
