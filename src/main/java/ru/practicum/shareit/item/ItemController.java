package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;


@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
private final ItemService itemService;
@PostMapping
    public ItemDto createItem(@RequestHeader ("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
    return itemService.createItem(itemDto, userId);
}
}
