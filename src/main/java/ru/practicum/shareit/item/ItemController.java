package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") Long userId,@Valid @RequestBody Item item) {
        return itemService.add(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto,
                       @PathVariable Long itemId) {
        return itemService.update(userId, itemDto, itemId);
    }

    @GetMapping
    public List<Item> getAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllForUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getOne(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.getOne(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemService.search(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
    }
}
