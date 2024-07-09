package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RightsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.Utils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImp {
    private final ItemRepository repository;
    private final UserService userService;

    public ItemDto create(ItemDto itemDto, Long userId) {
        userService.validateUserId(userId);
        validate(itemDto);

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(userId);
        item.setId(Utils.getNextId(repository.findAll()));

        repository.create(item);
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto update(ItemDto item, Long itemId, Long userId) {
        Item oldItem = validateItemId(itemId);
        userService.validateUserId(userId);
        verificationRights(oldItem, userId);

        repository.update(updateItemFields(oldItem, item));
        return ItemMapper.mapToItemDto(oldItem);
    }

    public ItemDto get(Long itemId) {
        return ItemMapper.mapToItemDto(validateItemId(itemId));
    }

    public List<ItemDto> getAll(Long userId) {
        return repository.findAll(userId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    public List<ItemDto> find(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return repository.findAll(text).stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::mapToItemDto).toList();
    }

    private Item updateItemFields(Item original, ItemDto update) {
        if (update.getName() != null && !update.getName().isEmpty()) {
            original.setName(update.getName());
        }
        if (update.getDescription() != null && !update.getDescription().isEmpty()) {
            original.setDescription(update.getDescription());
        }
        if (update.getAvailable() != null) {
            original.setAvailable(update.getAvailable());
        }
        return original;
    }

    private Item validateItemId(Long id) {
        return repository.get(id).orElseThrow(() -> new NotFoundException(id));
    }

    private void validate(ItemDto item) {
        if (item.getAvailable() == null)
            throw new ValidationException("available is empty");
        if (item.getName() == null || item.getName().isEmpty() || item.getName().isBlank())
            throw new ValidationException("name is empty");
        if (item.getDescription() == null || item.getDescription().isEmpty() || item.getDescription().isBlank())
            throw new ValidationException("description is empty");
    }

    private void verificationRights(Item item, Long userId) {
        if (item.getOwnerId() != null && !item.getOwnerId().equals(userId)) {
            throw new RightsException();
        }
    }

}
