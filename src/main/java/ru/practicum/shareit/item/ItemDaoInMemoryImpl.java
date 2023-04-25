package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class ItemDaoInMemoryImpl implements ItemDao {
    private long idGenerator;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        Long id = getNextId();
        item.setId(id);
        items.put(id, item);
        log.info("new item added: {}", item.getName());
        return item;
    }

    @Override
    public Item update(Item item) {
        checkItemInStorage(item.getId());

        Item updatedItem = items.get(item.getId());

        if (!updatedItem.getOwner().equals(item.getOwner())) {
            throw new WrongUserException("wrong user:" + item.getOwner() + " is not an owner of " + item);
        }

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return updatedItem;
    }

    @Override
    public Item findById(Long itemId) {
        checkItemInStorage(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllByUserId(Long userId) {
        List<Item> result = new ArrayList<>();

        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) {
                result.add(item);
            }
        }

        return result;
    }

    @Override
    public List<Item> findByRequest(String request) {
        List<Item> result = new ArrayList<>();
        request = request.toLowerCase();

        for (Item item : items.values()) {
            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();

            if (item.getAvailable().equals(true) && (name.contains(request) || description.contains(request))) {
                result.add(item);
            }
        }
        return result;
    }

    private Long getNextId() {
        return ++idGenerator;
    }

    private void checkItemInStorage(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ObjectNotFoundException("item with id:" + itemId + " not found error");
        }
    }

    public void clearDataForTesting() {
        idGenerator = 0;
        items.clear();
    }
}