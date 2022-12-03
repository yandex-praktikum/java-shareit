package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    public ItemDto getItemById(Long itemId) {
        log.info("Получение товара с ID = {}", itemId);
        Item item = itemDao.getItemById(itemId);
        if (item == null) {
            throw new EntityNotFoundException("Товар с ID " + itemId + " не найден");
        }
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> getAllItems(Long userId) {
        log.info("Получение всех товаров");
        List<Item> items = itemDao.getAllItems(userId);
        return items.stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
    }

    public ItemDto createItem(Long userId, ItemDto itemDto) throws EntityNotFoundException {
        log.info("Создание нового товара {}", itemDto);
        userService.getUserById(userId);
        itemDto.setUserId(userId);
        Item item = itemDao.createItem(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws EntityNotFoundException {
        log.info("Обновление товара");
        userService.getUserById(userId);
        if (!itemDao.getItemById(itemId).getUserId().equals(userId)) {
            throw new EntityNotFoundException("Некорректное значение ID пользователя. Данные не обновлены!");
        }
        Item itemForUpdate = itemDao.updateItem(itemId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(itemForUpdate);
    }

    public List<ItemDto> searchItem(String searchText) {
        log.info("Поиск товара по строке {}", searchText);
        List<Item> items = itemDao.searchItem(searchText);
        return items.stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
    }
}
