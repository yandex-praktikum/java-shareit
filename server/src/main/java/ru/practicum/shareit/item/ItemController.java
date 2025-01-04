package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserIdMissingException;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.RequestCreateCommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.RequestItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> findAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        validateUserId(userId);
        return itemService.findAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByNameAndDescription(@RequestParam("text") String text) {
        return itemService.searchByNameAndDescription(text);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody RequestItemDto itemDto) {
        validateUserId(userId);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long itemId,
                          @RequestBody RequestItemDto itemDto) {
        validateUserId(userId);
        return itemService.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long itemId) {
        itemService.deleteById(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long itemId,
                                       @RequestBody RequestCreateCommentDto requestCreateCommentDto) {
        validateUserId(userId);
        return itemService.saveCommentToItem(userId, itemId, requestCreateCommentDto);
    }

    private void validateUserId(Long userId) {
        if (userId == null)
            throw new UserIdMissingException("Заголовок \"X-Sharer-User-Id\" отсутствует");
    }
}
