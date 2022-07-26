package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    ItemServiceImpl(ItemRepository repository, UserRepository userRepository) {
        this.itemRepository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Item addNewItem(int userId, Item item) {
        if (userRepository.getUserById(userId) != null
                && item.getAvailable() != null
                && item.getName() != null && !item.getName().isEmpty()
                && item.getDescription() != null && !item.getDescription().isEmpty()) {
            item.setUserId(userId);
            return itemRepository.addItem(item);
        } else if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("пользователь не найден");
        } else if (item.getAvailable() == null
                || item.getName() != null || !item.getDescription().isEmpty()
                || item.getDescription() != null || !item.getName().isEmpty()) {
            throw new ValidationException("Входные условия item");
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteItem(int userId, int itemId) {
        if (itemRepository.getItemById(itemId).getUserId() == userId) {
            itemRepository.deleteItem(itemId);
        } else {
            throw new AccessException("Доступ запрещен");
        }
    }

    @Override
    public List<Item> findItemByRequest(String text) {
        List<Item> result = new ArrayList<>();
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            log.info(itemRepository.getAllItems().toString());
            result = itemRepository.getAllItems().stream().filter(
                            item -> (item.getDescription().toLowerCase(Locale.ROOT)
                                    .contains(text.toLowerCase(Locale.ROOT))
                                    || item.getName().toLowerCase(Locale.ROOT)
                                    .contains(text.toLowerCase(Locale.ROOT)))
                                    && item.getAvailable())
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.getAllItems();
    }

    @Override
    public List<Item> getItemsByUserId(int userId) {
        return itemRepository.findItemByUserId(userId);
    }

    @Override
    public Item getItemById(int itemId) {
        return itemRepository.getItemById(itemId);
    }


    @Override
    public Item updateItem(int userId, int itemId, Item item) {
        Item oldItem = itemRepository.getItemById(itemId);

        if (userRepository.getUserById(userId) != null &&
                itemRepository.getItemById(itemId) != null && userId == oldItem.getUserId()) {
            if (item.getName() != null && !item.getName().isEmpty()) {
                oldItem.setName(item.getName());
                itemRepository.deleteItem(itemId);
            }
            if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                oldItem.setDescription(item.getDescription());
                itemRepository.deleteItem(itemId);
            }
            if (item.getAvailable() != oldItem.getAvailable() && item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
                itemRepository.deleteItem(itemId);
            }
            return itemRepository.addItem(oldItem);
        }
        throw new NotFoundException("Итернал");
    }

}
