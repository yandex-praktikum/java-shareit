package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId,
                           @Valid @RequestBody ItemDto itemDtoRequest) throws ValidationException {
        ItemDto itemDtoResponse = itemService.addItem(userId, itemDtoRequest);
        log.info("item has been added");
        return itemDtoResponse;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateUser(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable Integer itemId,
                              @RequestBody ItemDto itemDtoRequest) throws ValidationException {
        ItemDto itemDtoResponse = itemService.updateItem(userId, itemId, itemDtoRequest);
        log.info("item has been updated");
        return itemDtoResponse;
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable Integer itemId) throws ValidationException {
        ItemDto itemDto = itemService.findItemById(itemId);
        log.info("item has been found");
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> findAllItemsForUser(@RequestHeader("X-Sharer-User-Id") int userId) throws ValidationException {
        List<ItemDto> itemDtos = itemService.findAllItemsForUser(userId);
        log.info("items has been found");
        return itemDtos;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        List<ItemDto> itemDtos = itemService.searchItemsByText(text);
        log.info("items has been found by text " + text);
        return itemDtos;
    }
}
