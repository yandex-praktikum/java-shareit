package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDaoStorage;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDaoStorage itemDaoStorage;
    private final UserDaoStorage userDaoStorage;

    @Override
    public Item create(Long userId, ItemDto dto) {
        if (dto.getName() == null || dto.getDescription() == null || dto.getAvailable() == null) {
            throw new ValidationException("Все поля должны быть заполнены");
        } else {
            Optional<User> user = userDaoStorage.getUserById(userId);
            if (user.isPresent()) {
                return itemDaoStorage.create(userId, dto);
            }
            throw new IllegalArgumentException("Юзер с id " + userId + " не существует");
        }
    }

    @Override
    public Optional<Item> update(Long userId, Long itemId, Item item) {
        Optional<Item> itemForUpdating = getItemByIdForUser(userId,itemId);
        if (itemForUpdating.isPresent()) {
            return Optional.of(itemDaoStorage.update(userId, itemId, item))
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Item с id" + itemId + " после обновления не найден"));
        }
        throw new IllegalArgumentException("Item с id " + itemId + " перед обновлением не найден");
    }

    @Override
    public Optional<Item> getItemByIdForUser(Long userId, Long itemId) {
        Optional<User> user = userDaoStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Юзер с id " + userId + " не найден.");
        }
        return Optional.of(itemDaoStorage.getItemById(userId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item с id " + itemId + " не найден")));
    }

    @Override
    public Optional<Item> getItemByIdForAllUser(Long itemId) {
        return Optional.of(itemDaoStorage.getItemByIdForAllUser(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item с id " + itemId + " не найден")));
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemDaoStorage.findAll(userId);
    }

    @Override
    public List<Item> findItemByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemDaoStorage.findItemByText(text);
    }
}
