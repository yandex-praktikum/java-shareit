package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public Item createItem(Item item) {
        return itemStorage.createItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        Item currentItem = getItemById(item.getId());
        if (item.getOwner().getId() != currentItem.getOwner().getId()) {
            throw new NotFoundException("Only the owner can update a post");
        }
        if (item.getName() != null) {
            currentItem = currentItem.withName(item.getName());
        }
        if (item.getDescription() != null) {
            currentItem = currentItem.withDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            currentItem = currentItem.withAvailable(item.getAvailable());
        }
        return itemStorage.updateItem(currentItem);
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = itemStorage.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Item with id = " + itemId + " is not found");
        }
        return item;
    }

    @Override
    public List<Item> getUserItems(long ownerId) {
        return itemStorage.getUserItems(ownerId);
    }

    @Override
    public List<Item> findItemsWithText(String text) {
        return itemStorage.findItemsWithText(text);
    }
}