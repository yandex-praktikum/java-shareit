package ru.practicum.shareit.storage.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class ItemService implements ItemServiceInterface {
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    UserStorage userStorage;

    @Override
    public ItemDto addItem(ItemDto itemDto, int sharerUserId) {
        if (itemDto.getAvailable() == null) {
            throw new BadRequestException("Укажите доступность предмета к аренде");
        }
        Item item = itemDto.fromItemDto(itemDto);
        item.setOwner(userStorage.getUserById(sharerUserId));
        if (item.getOwner() == null) {
            throw new NotFoundException("Такого пользователя нет.");
        } else {
            isValidItem(item);
        }

        log.info("Adding item " + item.getName());
        Item returnedItem = itemStorage.addItem(item);
        ItemDto returnedDto = returnedItem.toItemDto(returnedItem);
        return returnedDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int sharerUserId, int itemId) {
        Item item = itemDto.fromItemDto(itemDto);
        Item itemToCheckOwner = itemStorage.getItemById(itemId);
        if (itemToCheckOwner.getOwner() == null || itemToCheckOwner.getOwner().getId() != sharerUserId) {
            throw new NotFoundException("Не совпадает идентификатор пользователя");
        }
        item.setId(itemId);

        //isValidItem(item);
        if (itemStorage.updateItem(item) == null) {
            throw new BadRequestException("Такого предмета нет");
        } else {
            log.info("Updating item " + item.getName());
            Item returnedItem = itemStorage.getItemById(item.getId());
            ItemDto returnedDto = returnedItem.toItemDto(returnedItem);
            return returnedDto;
        }
    }

    @Override
    public ItemDto getItemById(int id) {
        if (itemStorage.getItemById(id) == null) {
            throw new BadRequestException("Такого предмета нет");
        } else {
            log.info("Getting item " + id);
            Item returnedItem = itemStorage.getItemById(id);
            ItemDto returnedDto = returnedItem.toItemDto(returnedItem);
            return returnedDto;
        }
    }

    @Override
    public List<Item> getAllItemsByOwnerId(int id) {
        if (id <= 0) {
            throw new BadRequestException("Неверно указан id пользователя");
        }
        return itemStorage.getAllItemsByOwnerId(id);
    }

    @Override
    public List<Item> searchItem(String keyWord) {
        return itemStorage.searchItem(keyWord);
    }

    public void isValidItem(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new BadRequestException("Не указано называние предмета");
        } else if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new BadRequestException("Не указано описание предмета");
        } else if (item.getOwner() == null) {
            throw new ValidationException("Не указан собственник предмета");
        }
    }
}



