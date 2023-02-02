package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.utilities.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;
    private final ItemValidator validator;

    @GetMapping
    public List<ItemDtoResponse> getAllByUserId(@Valid @RequestHeader(Constants.USER_ID_HEADER) Long id) {
        return service.getAllByUserId(id);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse getItemById(@Valid @PathVariable Long itemId,
                                       @RequestHeader(Constants.USER_ID_HEADER) Long ownerId) {
        return service.getItemById(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@NotBlank @NotNull @RequestParam String text) {
        return service.getItemsByText(text);
    }

    @PostMapping
    public ItemDto add(@Valid @RequestBody ItemDto item,
                             @NotNull @RequestHeader(Constants.USER_ID_HEADER) Long id) {
        validator.userValidator(id);
        validator.itemAddValidation(item);
        return service.add(item, id);
    }

    @PatchMapping("/{itemId}")
    public Item update(@Valid @RequestBody ItemDto item,
                       @NotNull @RequestHeader(Constants.USER_ID_HEADER) Long id,
                       @PathVariable Long itemId) {
        return service.update(item, itemId, id);
    }

    @DeleteMapping
    public Item delete(@Valid @RequestBody ItemDto item,
                       @RequestHeader(Constants.USER_ID_HEADER) Long id) {
        return service.delete(item, id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return service.addComment(commentDto, userId, itemId);
    }
}
