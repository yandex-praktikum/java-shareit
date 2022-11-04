package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDaoStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserDaoStorage;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDaoStorage itemDaoStorage;
    private final UserDaoStorage userDaoStorage;
    private final ItemMapper mapper;

    @Override
    public ItemDto create(Long userId, ItemDto dto) {
        if (dto.getAvailable() == null) {
            throw new ValidationException("Все поля должны быть заполнены");
        } else {
            Optional<User> user = userDaoStorage.getUserById(userId);
            if (user.isPresent()) {
                Item item = mapper.fromItemDtoCreate(dto);
                Item createdItem = itemDaoStorage.addToUserItemsList(userId, item);
                return mapper.fromItem(createdItem);
            }
            throw new IllegalArgumentException("Юзер с id " + userId + " не существует");
        }
    }

    @Override
    public Optional<ItemDto> update(Long userId, Long itemId, ItemDto itemDto) {
        Optional<ItemDto> itemForUpdating = getItemByIdForUser(userId,itemId);
        if (itemForUpdating.isPresent()) {
            Item item = mapper.fromItemDtoUpdate(itemDto);
            Item updatedItem = itemDaoStorage.update(userId, itemId, item).orElseThrow(
                    () -> new IllegalArgumentException("Item с id" + itemId + " после обновления не найден"));
            return Optional.of(mapper.fromItem(updatedItem));
        }
        throw new IllegalArgumentException("Item с id " + itemId + " перед обновлением не найден");
    }

    @Override
    public Optional<ItemDto> getItemByIdForUser(Long userId, Long itemId) {
        Optional<User> user = userDaoStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Юзер с id " + userId + " не найден.");
        }
        Item item = itemDaoStorage.getItemById(userId, itemId).orElseThrow(
                () -> new IllegalArgumentException("Item с id " + itemId + " не найден"));
        return Optional.of(mapper.fromItem(item));
    }

    @Override
    public Optional<ItemDto> getItemByIdForAllUser(Long itemId) {
        Item item = itemDaoStorage.getItemByIdForAllUser(itemId).orElseThrow(
                () -> new IllegalArgumentException("Item с id " + itemId + " не найден"));
        return Optional.of(mapper.fromItem(item));
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        List<Item> responseItemList = itemDaoStorage.findAll(userId);
        if (responseItemList.isEmpty()) {
            throw new IllegalArgumentException("Ни один айтем не найден");
        }
        return responseItemList.stream()
                .map(mapper::fromItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemDaoStorage.findItemByText(text).stream()
                .map(mapper::fromItem)
                .collect(Collectors.toList());
    }

}
