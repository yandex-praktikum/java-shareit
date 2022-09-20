package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @Valid @RequestBody ItemDto itemDto) {
        return itemService.update(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    private void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        itemService.delete(id, userId);
    }

    @GetMapping("/{id}")
    private Item getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemService.find(id);
    }

    @GetMapping("")
    private List<Item> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findUserItems(userId);
    }

    @GetMapping("/search")
    private List<Item> searching(@RequestParam String text) {
        return itemService.findItemsByText(text);
    }

}
