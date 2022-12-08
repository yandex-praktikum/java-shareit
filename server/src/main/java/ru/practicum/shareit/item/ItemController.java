package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoDate;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto add(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        Item item = itemMapper.toItem(itemDto, userId, null);
        return itemMapper.toDto(itemService.add(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Integer itemId,
                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        Item item = itemMapper.toItem(itemDto, userId, itemId);
        return itemMapper.toDto(itemService.update(item, userId));
    }

    @GetMapping("/{itemId}")
    public ItemDtoDate getById(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItemDate(itemId, LocalDateTime.now(), userId);
    }

    @GetMapping
    public Collection<ItemDtoDate> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getByNameOrDesc(@RequestParam String text,
                                               @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        ArrayList<ItemDto> listDto = new ArrayList<>();
        for (Item item : itemService.getByNameOrDesc(text, from, size)) {
            listDto.add(itemMapper.toDto(item));
        }
        return listDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentDto commentDto, @PathVariable Integer itemId,
                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }

}
