package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.validation.Validation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    @Override
    public ItemDto addItem(int userId, ItemDto itemDto) throws ValidationException {
        Validation.validateUserId(userStorage, userId);
        return itemStorage.addItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) throws ValidationException {
        Validation.validateUserId(userStorage, userId);
        Validation.validateItemId(itemStorage, itemId);
        Validation.validateUserIdForItem(itemStorage, userId, itemId);
        if (itemDto.getName() == null) {
            itemDto.setName(itemStorage.findItemById(itemId).getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemStorage.findItemById(itemId).getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemStorage.findItemById(itemId).getAvailable());
        }
        return itemStorage.updateItem(userId, itemId, itemDto);
    }

    @Override
    public ItemDto findItemById(Integer itemId) throws ValidationException {
        Validation.validateItemId(itemStorage, itemId);
        return itemStorage.findItemDtoById(itemId);
    }

    @Override
    public List<ItemDto> findAllItemsForUser(int userId) throws ValidationException {
        Validation.validateUserId(userStorage, userId);
        return itemStorage.findAllItemsForUser(userId);
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        String textToLowerCase = text.toLowerCase();
        return itemStorage.findAllItems().stream()
                .filter(x -> x.getDescription().toLowerCase().contains(textToLowerCase))
                .filter(x -> x.getAvailable().equals(true))
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }
}
