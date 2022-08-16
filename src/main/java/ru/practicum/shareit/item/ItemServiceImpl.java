package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;


    public ItemDto getItemById(long userId, long itemId) {
        if (userId <= 0 & itemId <= 0) {
            log.info("ID равно 0");
            throw new ValidationException("ID меньше или равно 0");
        }
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        if (userId <= 0) {
            log.info("ID пользователя равен 0");
            throw new ValidationException("ID меньше или равно 0");
        }
        return itemRepository.findByUserId(userId);
    }

    @Override
    public ItemDto add(long userId, ItemDto itemDto) throws NotFoundException {
        if (userId <= 0 || userService.get(userId) == null) {
            throw new ValidationException("ID меньше или равно 0");
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            log.info("Нет наименования вещи");
            throw new ValidationException("Нет наименования вещи");
        }
        if (itemDto.getDescription() == null) {
            log.info("Нет описания вещи");
            throw new ValidationException("Нет описания вещи");
        }
        if (itemDto.getAvailable() == null) {
            log.info("Нет статуса доступности вещи");
            throw new ValidationException("Отсутствует статус доступности вещи");
        }
        return itemRepository.save(userId, itemDto);
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException {
        if (userId <= 0) {
            log.info("ID пользователя равно 0");
            throw new ValidationException("ID пользователя меньше или равно 0");
        }
        if (userService.get(userId) == null) {
            log.info("Пользователь {} не существует", userId);
            throw new ValidationException("Пользователь " + userId + " не существует");
        }
        return itemRepository.updateItem(userId, itemId, itemDto);
    }

    @Override
    public void delete(long userId, long itemId) {
        if (userId <= 0) {
            log.info("ID пользователя меньше или равно 0");
            throw new ValidationException("ID пользователя меньше или равно 0");
        }
        if (itemId <= 0) {
            log.info("ID вещи меньше или равно 0");
            throw new ValidationException("ID вещи меньше или равно 0");
        }
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<ItemDto> search(long userId, String search) {
        if (userId <= 0) {
            log.info("ID пользователя меньше 0");
            throw new ValidationException("ID пользователя меньше 0");
        }
        if (search.isEmpty()) {
            log.info("Строка поиска пустая");
            return new ArrayList<>();
        }
        return itemRepository.searchItem(search);
    }
}