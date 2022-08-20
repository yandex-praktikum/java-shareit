package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.validator.Marker.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping("{itemId}")
    public ItemOutputDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemOutputDto> getOwnItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getOwnItems(ownerId);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public ItemInputDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @Valid @RequestBody ItemInputDto itemInputDto) {
        return itemService.create(ownerId, itemInputDto);
    }

    @PatchMapping("{id}")
    @Validated(OnUpdate.class)
    public ItemInputDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @PathVariable Long id,
                               @Valid @RequestBody ItemInputDto itemInputDto) {
        return itemService.update(ownerId, id, itemInputDto);
    }

    @GetMapping("search")
    public List<ItemInputDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto) {
        return itemService.createComment(bookerId, itemId, commentDto);
    }

}
