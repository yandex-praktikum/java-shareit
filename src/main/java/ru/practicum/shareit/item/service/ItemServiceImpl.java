package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;

    private final List<Item> items = new ArrayList<>();
    private final AtomicLong atomicId = new AtomicLong();

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (itemDto.getAvailable() == null ||
                itemDto.getName() == null || itemDto.getName().isBlank() ||
                itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ErrorException();
        }
        itemDto.setId(atomicId.addAndGet(1));
        itemDto.setOwnerId(userId);
        Item item = ItemMapper.mapItemDtoToItem(itemDto);
        items.add(item);
        return ItemMapper.mapItemToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Optional<Item> itemForUpdate = items.stream()
                .filter(item -> (item.getId().equals(itemDto.getId()) || item.getId().equals(itemId)) && item.getOwnerId().equals(userId))
                .findFirst();
        if (itemForUpdate.isPresent()) {
            itemForUpdate.get().setAvailable((itemDto.getAvailable() != null ? itemDto.getAvailable() : itemForUpdate.get().getAvailable()));
            itemForUpdate.get().setRequest(itemDto.getRequest() != null ? itemDto.getRequest() : itemForUpdate.get().getRequest());
            itemForUpdate.get().setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : itemForUpdate.get().getDescription());
            itemForUpdate.get().setName(itemDto.getName() != null ? itemDto.getName() : itemForUpdate.get().getName());
            return ItemMapper.mapItemToItemDto(itemForUpdate.get());
        }

        throw new NotFoundException("Ошибка");
    }

    @Override
    public List<Item> returnListOfItems(Long userId) {
        return items.stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> itemByText(Long userId, String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return items.stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase(Locale.ROOT))
                                || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(long itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst().orElse(null);
    }


}
