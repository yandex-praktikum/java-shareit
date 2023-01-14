package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    @Override
    public ItemDto get(Long id) {
        return null;
    }

    @Override
    public Collection<ItemDto> getAllByUserId(Long userId) {
        return null;
    }

    @Override
    public ItemDto add(ItemDto itemDto, Long ownerId) {
        return null;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public Collection<ItemDto> search(String keyword, Long userId) {
        return null;
    }
}
