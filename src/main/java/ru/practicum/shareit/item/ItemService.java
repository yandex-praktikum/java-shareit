package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<ItemDto> getUserItems(Long userId) {
        return itemRepository.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItem(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItem(itemId));
    }

    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        ItemValidator.checkAllFields(itemDto);
        return ItemMapper.toItemDto(itemRepository.createItem(ItemMapper.toItem(itemDto, ownerId)));
    }

    public ItemDto updateItem(ItemDto updatedItemDto, Long redactorId) {
        ItemValidator.checkNotNullFields(updatedItemDto);
        return ItemMapper.toItemDto(itemRepository.updateItem(ItemMapper.toItem(updatedItemDto, null),
                redactorId));
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank())
            return new ArrayList<>();
        return itemRepository.getItems().stream()
                .filter(item ->
                        (item.getAvailable() &&
                                (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        )) // Отбираем доступные вещи, подходящие по тексту
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
