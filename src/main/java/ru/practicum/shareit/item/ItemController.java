package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public List<ItemDto> getAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String search) {
        return itemService.searchItems(search);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable("id") long id, @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(id, userId, itemDto);
    }
}
