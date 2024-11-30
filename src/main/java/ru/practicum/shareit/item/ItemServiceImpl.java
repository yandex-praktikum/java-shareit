package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemInMemoryRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserInMemoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemInMemoryRepository itemInMemoryRepository;
    private final UserInMemoryRepository userInMemoryRepository;

    @Override
    public List<ItemDto> getItems(long userId) {
        log.info("ItemServiceImpl: Запрос вещей пользователя с ID={}", userId);
        return Optional.of(itemInMemoryRepository.getItems(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())).get();
    }

    public ItemDto getItem(long itemId) {
        log.info("ItemServiceImpl: Запрос вещи с ID={}", itemId);
        return itemInMemoryRepository.getItem(itemId);
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto item) {
        log.info("ItemServiceImpl: Запрос на добавление вещи {}", item);
        if (userInMemoryRepository.getUserById(userId).isEmpty()) {
            log.info("Такого пользователя нет");
            throw new NotFoundException("Такого пользователя нет");
        }
        Item newItem = ItemMapper.toItem(item);
        newItem.setOwner(userId);
        if (validate(newItem)) {
            newItem = itemInMemoryRepository.addNewItem(userId, newItem);
            return ItemMapper.toItemDto(newItem);
        }
        return null;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        log.info("ItemServiceImpl: Запрос на удаление вещи с ID={}", itemId);
        itemInMemoryRepository.deleteItem(userId, itemId);
    }

    @Override
    public ItemDto changeItem(long userId, long itemId, ItemDto item) {
        log.info("ItemServiceImpl: Запрос на обновление вещи с ID={}", itemId);
        if (userInMemoryRepository.getUserById(userId).isEmpty()) {
            log.info("Такого пользователя нет");
            throw new NotFoundException("Такого пользователя нет");
        }
        Item newItem = ItemMapper.toItem(item);
        newItem.setOwner(userId);
        newItem = itemInMemoryRepository.changeItem(userId, itemId, newItem);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public List<ItemDto> searchItem(String textToSearch) {
        log.info("ItemServiceImpl: Запрос на поиск вещи по тексту \"{}\"", textToSearch);
        return itemInMemoryRepository.searchItem(textToSearch);
    }

    private boolean validate(Item item) {
        boolean result = true;
        if ((item.getDescription() == null) || (item.getName().isEmpty())) {
            result = false;
            throw new ValidationException("Название вещи не может быть пустым");
        }
        if ((item.getDescription() == null) || (item.getDescription().isEmpty())) {
            result = false;
            throw new ValidationException("Описание вещи не может быть пустым");
        }
        if (item.getOwner() == null) {
            result = false;
            throw new ValidationException("У вещи должен быть хозяин");
        }
        if (item.getAvailable() == null) {
            result = false;
            throw new ValidationException("Для вещи должна быть указана доступность");
        }
        return result;
    }
}
