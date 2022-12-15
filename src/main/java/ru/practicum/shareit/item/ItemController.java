package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemController {
    ItemService itemServiceImpl;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        itemDto.setOwner(userId);
        return ItemMapper.toItemDto(itemServiceImpl.create(ItemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto,
                          @PathVariable("itemId") Long id) {
        itemDto.setOwner(userId);
        return ItemMapper.toItemDto(itemServiceImpl.update(ItemMapper.toItem(itemDto), id));
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable("itemId") Long id) {
        return ItemMapper.toItemDto(itemServiceImpl.getById(id));
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.getAll(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemServiceImpl.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
