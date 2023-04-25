package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @NotNull @Min(1) @RequestHeader(USER_ID) Long userId) {
        Item item = ItemMapper.toItemModel(itemDto, userId);
        return ItemMapper.toItemDto(itemService.create(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @NotNull @Min(1) @PathVariable Long itemId,
                          @NotNull @Min(1) @RequestHeader(USER_ID) Long userId) {
        Item item = ItemMapper.toItemModel(itemDto, userId);
        item.setId(itemId);
        return ItemMapper.toItemDto(itemService.update(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@NotNull @Min(1) @PathVariable Long itemId) {
        return ItemMapper.toItemDto(itemService.findById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@NotNull @Min(1) @RequestHeader(USER_ID) Long userId) {
        List<Item> userItems = itemService.getAllByUserId(userId);
        return ItemMapper.toItemDtoList(userItems);
    }

    @GetMapping("/search")
    public List<ItemDto> findByRequest(@RequestParam String text) {
        List<Item> foundItems = itemService.findByRequest(text);
        return ItemMapper.toItemDtoList(foundItems);
    }
}