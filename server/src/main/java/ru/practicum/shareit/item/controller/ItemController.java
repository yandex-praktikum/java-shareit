package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private ItemService itemServiceImpl;

    @PostMapping
    public ItemDto create(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                          @RequestBody ItemDto itemDto) {

        return itemServiceImpl.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable long itemId) {

        return itemServiceImpl.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemBookingDto get(@PathVariable long itemId,
                              @RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemServiceImpl.getItemById(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                       @PathVariable long itemId) {
        itemServiceImpl.delete(userId, itemId);
    }

    @GetMapping
    public List<ItemBookingDto> getAllUsersItems(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "25") int size) {
        return itemServiceImpl.getAllUsersItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "25") int size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemServiceImpl.search(text.toLowerCase(Locale.ROOT), from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                    @PathVariable long itemId,
                                    @RequestBody CommentDto commentDto) {

        return itemServiceImpl.createComment(commentDto, itemId, userId);
    }
}
