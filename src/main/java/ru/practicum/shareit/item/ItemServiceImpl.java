package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.ValidationUtil;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item add(ItemDto itemDto, int ownerId) {
        log.info("ItemService: add({},{})", itemDto, ownerId);
        Item item = ItemMapper.toItem(itemDto, ownerId);
        ValidationUtil.checkFound(item.getId(), String.valueOf(item.getId()));
        ValidationUtil.checkNotFound(userRepository.get(ownerId), String.valueOf(ownerId));
        return itemRepository.save(item);
    }

    @Override
    public Item update(ItemDto itemDto, int itemId, int ownerId) {
        log.info("ItemService: update({},{}, {})", itemDto, itemId, ownerId);
        Item myItem = ValidationUtil.checkNotFound(get(itemId), "item");
        if (ownerId != myItem.getOwnerId()) {
            throw new NotFoundException("Пользователь не является владельцем вещи.");
        }
        if (itemDto.getName() != null) {
            myItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            myItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            myItem.setAvailable(itemDto.getAvailable());
        }
        return itemRepository.update(myItem);
    }

    @Override
    public Item get(int itemId) {
        log.info("ItemService: get({})", itemId);
        return ValidationUtil.checkNotFoundWithId(itemRepository.get(itemId), itemId, "item");
    }

    @Override
    public List<Item> getOwnerItems(int ownerId) {
        log.info("ItemService: getOwnerItems({})", ownerId);
        ValidationUtil.checkNotFound(userRepository.get(ownerId), String.valueOf(ownerId));
        return itemRepository.getOwnerItems(ownerId);
    }

    @Override
    public List<Item> find(String text) {
        log.info("ItemService: find({})", text);
        return itemRepository.find(text);
    }
}
