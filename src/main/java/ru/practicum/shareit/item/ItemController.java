package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("{itemId}")
    public Optional<Item> getItemById(@PathVariable Long itemId) {
        return itemService.getItemByIdForAllUser(itemId);
    }

    @GetMapping("search")
    public List<Item> getItemByText(@RequestParam String text) {
        return itemService.findItemByText(text);
    }

    @PostMapping
    public Item addItemToUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Optional<Item> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId, @RequestBody Item item) {
        return itemService.update(userId, itemId, item);
    }
}
