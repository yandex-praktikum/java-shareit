package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundexception;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@Valid @RequestBody Item item,
                           @RequestHeader Map<String, String> headers) throws NotFoundexception, BadRequestException {

        return itemService.addItem(item, headers);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable(value = "id") Long itemId,
                              @RequestBody Item item, @RequestHeader Map<String, String> headers) throws NotFoundexception {

        return itemService.updateItem(itemId, item, headers);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader Map<String, String> headers) {
        return itemService.getItems(headers);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable(value = "id") Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }

}
