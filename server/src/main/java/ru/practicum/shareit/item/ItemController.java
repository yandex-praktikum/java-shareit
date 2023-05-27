package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utilits.Variables;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor

public class ItemController {
    private final ItemService itemService;

    private final String pathId = "/{id}";
    private final String pathComment = "/{id}/comment";

    @GetMapping()
    public List<ItemDto> getItems(@RequestHeader(value = Variables.USER_ID) Integer ownerId) {
        return itemService.getItems(ownerId);
    }

    @GetMapping(pathId)
    public ItemDto getItem(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                           @PathVariable Integer id) {
        return itemService.getItem(ownerId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> getItems(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                  @RequestParam(name = "text") String text) {
        return itemService.getItems(text);
    }

    @PostMapping()
    public ItemDto create(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                          @Valid @RequestBody @NotNull ItemDto item) {
        return itemService.addItem(ownerId, item);
    }

    @PatchMapping(pathId)
    public ItemDto update(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                          @PathVariable Integer id,
                          @Valid @RequestBody @NotNull ItemDto item) {
        return itemService.update(ownerId, id, item);
    }

    @PostMapping(pathComment)
    public CommentDto addComment(@RequestHeader(value = Variables.USER_ID) Integer authorId,
                              @PathVariable Integer id,
                              @Valid @RequestBody @NotNull CommentDto commentBody) {
        return itemService.addComment(authorId, id, commentBody);
    }

}
