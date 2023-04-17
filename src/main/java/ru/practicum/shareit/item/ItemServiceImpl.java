package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.DtoInItem;
import static ru.practicum.shareit.item.ItemMapper.itemInDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private Long currentId = 0L;

    @Override
    public ItemDto update(ItemDto inputItemDto, Long ownerId, Long itemId) {
        ItemDto oldItem = findItemById(itemId);
        if (!ownerId.equals(oldItem.getOwner())) {
            throw new NotFoundException("Попытка обновления не принадлежащей владельцу вещи");
        }
        if (inputItemDto.getName() != null) {
            oldItem.setName(inputItemDto.getName());
        }
        if (inputItemDto.getDescription() != null) {
            oldItem.setDescription(inputItemDto.getDescription());
        }
        if (inputItemDto.getAvailable() != null) {
            oldItem.setAvailable(inputItemDto.getAvailable());
        }
        Item item = itemRepository.update(DtoInItem(oldItem));
        return itemInDto(item);
    }

    @Override
    public ItemDto create(ItemDto inputItemDto, Long ownerId) {
        inputItemDto.setId(++currentId);
        Item newItem = itemRepository.create(ownerId, DtoInItem(inputItemDto));
        return itemInDto(newItem);
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с id: " + itemId + " не найдена");
        }
        return itemInDto(item);
    }

    @Override
    public List<ItemDto> findAllItemsOwner(Long id) {
        return itemRepository.findAllItemsOwner(id)
                .stream()
                .map(ItemMapper::itemInDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemRepository.searchItem(text)
                .stream()
                .map(ItemMapper::itemInDto)
                .collect(Collectors.toList());
    }
}
