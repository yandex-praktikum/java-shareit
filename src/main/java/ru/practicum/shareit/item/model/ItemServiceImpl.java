package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoSuchUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserRepositoryImpl userRepository;

    @Override
    public ItemDto addItem(Item item, long ownerId) {
        if (userRepository.isUserExist(ownerId)) {

            return repository.addItem(item, ownerId);
        }
        throw new NoSuchUserException(String.format("There is no User with ID=%s.", ownerId));
    }

    @Override
    public ItemDto updateItem(Item item, long ownerId, long itemId) {
        if (userRepository.isUserExist(ownerId)) {
            return repository.updateItem(item, ownerId, itemId);
        }
        throw new NoSuchUserException(String.format("There is no User with ID=%s.", ownerId));
    }

    @Override
    public ItemDto getItem(long itemId) {
        return repository.getItem(itemId);
    }

    @Override
    public List<ItemDto> getUsersOwnItems(long ownerId) {
        if (userRepository.isUserExist(ownerId)) {
            return repository.getUsersOwnItems(ownerId);
        }
        throw new NoSuchUserException(String.format("There is no User with ID=%s.", ownerId));
    }

    @Override
    public List<ItemDto> searchItemByDescription(String searchText) {
        return repository.searchItemByDescription(searchText);
    }
}