package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;
/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    protected ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody Item item) {
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody Item item,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable long itemId) {
        return itemService.updateItem(item, userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getUsersOwnItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getUsersOwnItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.searchItemByDescription(text);
    }

}