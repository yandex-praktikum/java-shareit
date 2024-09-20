package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictExceptions;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.fromItemDto(itemDto);
        return ItemMapper.toItemDto(itemRepository.addItem(userService.getUserById(userId), item));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        getItemById(userId, itemId);
        Item item = itemRepository.getItemById(itemId).get();
        if (!item.getOwner().getId().equals(userId)) {
            throw new ConflictExceptions("Вещь не пренадлежит пользователю с id " + userId);
        }
        return ItemMapper.toItemDto(itemRepository.updateItem(item, itemDto));
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        userService.getUserById(userId);
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена")));
    }

    @Override
    public List<ItemDto> getItemsFromUsers(Long userId) {
        userService.getUserById(userId);
        return itemRepository.getItemsFromUsers(userId);
    }

    @Override
    public List<ItemDto> search(String search, Long userId) {
        userService.getUserById(userId);
        if (search.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(search);
    }
}
