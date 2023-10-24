package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private UserDao userDao;

    @Override
    public ItemDto addItem(int userId, Item item) {
        userDao.getUserById(userId);
        return itemDao.addItem(userId, item);
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields) {
        return itemDao.updateItem(userId, itemId, fields);
    }

    @Override
    public ItemDto getItemDtoById(int itemId) {
        return itemDao.getItemDtoById(itemId);
    }

    @Override
    public List<ItemDto> getAllItemForOwner(int ownerId) {
        return itemDao.getAllItemForOwner(ownerId);
    }

    @Override
    public List<ItemDto> searchItem(String request) {
        return itemDao.searchItem(request);
    }
}
