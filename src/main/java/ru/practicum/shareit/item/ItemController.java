package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ExeptionNotFound;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private static final String OWNER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestBody ItemDto inputItemDto,
                          @RequestHeader(OWNER) Long owner) {
        userService.findUserById(owner);
        checkingCreating(inputItemDto);
        ItemDto createItem = itemService.create(inputItemDto, owner);
        log.debug("Добавление вещи пользователем: {}", owner);
        return createItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto inputItemDto,
                          @RequestHeader(OWNER) Long owner,
                          @PathVariable Long itemId) {
        ItemDto updateItem = itemService.update(inputItemDto, owner, itemId);
        log.debug("Обновление вещи с id: {}", itemId);
        return updateItem;
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable Long itemId) {
        ItemDto itemDto = itemService.findItemById(itemId);
        log.debug("Просмотр вещи с id: {}", itemId);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader(OWNER) Long owner) {
        List<ItemDto> allItems = itemService.findAllItemsOwner(owner);
        log.debug("Получение списка всех вещей");
        return allItems;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(value = "text") String text) {
        if (!text.isBlank()) {
            List<ItemDto> itemDtoList = itemService.searchItem(text);
            log.debug("Поиск необходимой вещи");
            return itemDtoList;
        }
        return new ArrayList<>();
    }

    private void checkingCreating(ItemDto inputItemDto) {
        if (inputItemDto.getName().isBlank() ||
                inputItemDto.getName() == null ||
                inputItemDto.getDescription() == null ||
                inputItemDto.getDescription().isBlank() ||
                inputItemDto.getAvailable() == null) {
            throw new ExeptionNotFound("Ошибка! Не все поля заполнены");
        }
    }
}
