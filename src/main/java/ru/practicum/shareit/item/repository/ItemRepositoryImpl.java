package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Qualifier("InMemory")
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private Long count = 0L;

    /**
     * Добавить вещь в репозиторий.
     * @param item добавленная вещь.
     * @return добавленная вещь.
     */
    @Override
    public Item add(Item item) {
        item.setId(++count);
        itemMap.put(item.getId(), item);
        return item;
    }

    /**
     * Получить список всех вещей.
     * @return список вещей.
     */
    @Override
    public List<Item> getAllItems(Long userId) {
        return itemMap.values().stream().filter(i -> i.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Получить вещь по ID.
     * @param id ID вещи.
     * @return запрашиваемая вещь.
     */
    @Override
    public Item getItemById(Long id) {
        return itemMap.get(id);
    }

    /**
     * Есть ли вещь с ID в хранилище?
     * @param id ID запрашиваемой вещи.
     * @return True - вещь есть в хранилище, False - вещи нет в хранилище.
     */
    @Override
    public Boolean isExcludeItemById(Long id) {
        return itemMap.containsKey(id);
    }

    /**
     * Удалить вещь с ID из хранилища.
     * @param id ID удаляемой вещи.
     */
    @Override
    public void removeItemById(Long id) {
        itemMap.remove(id);
    }

    /**
     * Удалить вещи пользователя с ID = userId.
     * @param userId ID пользователя, вещи которого надо удалить.
     */
    @Override
    public void removeItemsByUserId(Long userId) {
        List<Long> idForRemove = itemMap.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(Item::getId).collect(Collectors.toList());

        idForRemove.forEach(itemMap::remove);
    }

    /**
     * Обновить вещь в БД.
     * @param item вещь.
     * @return обновлённая вещь.
     */
    @Override
    public Item updateInStorage(Item item, boolean[] isUpdateField) {

        final Long inputId = item.getId();
        final String inputName = item.getName();
        final String inputDesc = item.getDescription();
        final Boolean inputAvailable = item.getAvailable();

        Item recordFromDB = itemMap.get(inputId);   //Вещь из БД.

        if (isUpdateField[0]) {
            recordFromDB.setName(inputName);
        }
        if (isUpdateField[1]) {
            recordFromDB.setDescription(inputDesc);
        }
        if (isUpdateField[2]) {
            recordFromDB.setAvailable(inputAvailable);
        }
        itemMap.put(inputId, recordFromDB);
        return recordFromDB;
    }

    /**
     * Поиск вещей по тексту.
     * @param text текст.
     * @return список вещей.
     */
    @Override
    public List<Item> searchItemsByText(String text) {
        return itemMap.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        item.getAvailable())
                .collect(Collectors.toList());
    }

}
