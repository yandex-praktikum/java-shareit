package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemValidation validation;

    @Override
    public Item add(Long userId, ItemDto itemDto) {
        validation.itemIsValidAdd(userId, itemDto);
        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setUserId(userId);
        return itemRepository.add(item);
    }

    @Override
    public Item update(Long userId, Long id, ItemDto itemDto) {

        validation.itemIsValidUpdate(userId, id);

        Item item = find(id);
        Item updatedItem = new Item(id, userId, item.getName(), item.getDescription(), item.getAvailable());

        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }

        return itemRepository.update(id, updatedItem);

    }

    @Override
    public void delete(Long id, Long userId) {
        validation.itemIsValidDelete(userId, id);
        itemRepository.delete(id);
    }

    @Override
    public Item find(Long id) {
        return itemRepository.find(id);
    }

    @Override
    public List<Item> findUserItems(Long userId) {
        return itemRepository.findUserItems(userId);
    }

    @Override
    public List<Item> findItemsByText(String text) {
        return itemRepository.findItemsByText(text);
    }
}
