package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemDto createItem(ItemDto itemDto, long userId) {
        return itemMapper.itemToItemDto(itemRepository.save(itemMapper.itemDtoToItem(itemDto)));
    }
}
