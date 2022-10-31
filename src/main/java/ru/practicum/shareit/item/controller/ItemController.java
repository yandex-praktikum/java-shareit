package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemService;
    @PostMapping
    public ItemDto createItem (@RequestHeader("X-Sharer-User-Id") Long userId,@Valid @RequestBody ItemDto itemDto){
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable Long id,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(itemDto, userId, id);
    }
    @GetMapping("/{id}")
    private ItemDto getItem(@PathVariable Long id){
        return itemService.getByID(id);
    }
    @GetMapping
    private List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId){
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    private  List<ItemDto> searchItem(@RequestParam String text){
        return itemService.searchItem(text);
    }
}
