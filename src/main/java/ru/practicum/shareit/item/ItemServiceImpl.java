package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public Item addNewItem(Long userId, Item item) {
        if (userRepository.getUserById(userId) == null) {
            throw new NoSuchElementException();
        }
        item.setOwnerId(userId);
        return repository.save(item);
    }

    @Override
    public Item updateItem(Long userId, ItemDto itemDto, Long itemId) {
        //validateUser(userDto.toUser());
        return repository.updateItem(userId, itemDto, itemId);
    }

    @Override
    public Item getItem(long userId, long itemId) {
        return repository.findByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<Item> getSearchedItems(String text) {
        return repository.getSearchedItems(text);
    }

    @Override
    public List<Item> getItems(long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndItemId(userId, itemId);
    }
}