package ru.practicum.shareit.item.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation_label.Create;
import ru.practicum.shareit.validation_label.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    public static final int VALID_ID = 1;
    public static final String ERROR_ITEM_ID = "ID вещи не может быть NULL ";
    public static final String ERROR_USER_ID = "ID пользователя не может быть NULL ";
    public static final String X_SHARER_USER = "X-Sharer-User-Id";

    private final ItemService itemService;
    private final ItemMapper mapper;

    public ItemController(ItemService itemService, ItemMapper mapper) {
        this.itemService = itemService;
        this.mapper = mapper;
    }

    @PostMapping
    public ItemDto createItem(@Validated({Create.class})
                              @RequestBody ItemDto itemDto,
                              @NotNull(message = (ERROR_ITEM_ID))
                              @Min(VALID_ID)
                              @RequestHeader(X_SHARER_USER) Long userId) {
        Item item = mapper.makeModel(itemDto, userId);
        return mapper.makeDto(itemService.createItem(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Validated({Update.class})
                              @RequestBody ItemDto itemDto,
                              @NotNull(message = ERROR_ITEM_ID)
                              @Min(VALID_ID)
                              @PathVariable Long itemId,
                              @NotNull(message = ERROR_USER_ID)
                              @Min(VALID_ID)
                              @RequestHeader(X_SHARER_USER) Long userId) {
        Item item = mapper.makeModel(itemDto, userId);
        item.setId(itemId);
        return mapper.makeDto(itemService.updateItem(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@NotNull(message = ERROR_ITEM_ID)
                                @Min(VALID_ID)
                                @PathVariable Long itemId) {
        return mapper.makeDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> findAllItems(@NotNull(message = ERROR_USER_ID)
                                      @Min(VALID_ID)
                                      @RequestHeader(X_SHARER_USER) Long userId) {
        List<Item> userItems = itemService.getAllItems(userId);
        return mapper.makeItemListToItemDtoList(userItems);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByRequest(@RequestParam String text) {
        List<Item> foundItems = itemService.getItemsByRequest(text);
        return mapper.makeItemListToItemDtoList(foundItems);
    }
}
