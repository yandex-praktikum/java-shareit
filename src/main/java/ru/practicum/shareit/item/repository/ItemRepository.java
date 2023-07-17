package ru.practicum.shareit.item.repository;


import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    /**
     * Добавить вещь в репозиторий.
     * @param item добавленная вещь.
     * @return добавленная вещь.
     */
    Item add(Item item);

    /**
     * Получить список всех вещей.
     * @return список вещей.
     */
    List<Item> getAllItems(Long userId);

    /**
     * Получить вещь по ID.
     * @param id ID вещи.
     * @return запрашиваемая вещь.
     */
    Item getItemById(Long id);

    /**
     * Есть ли вещь с ID в хранилище?
     * @param id ID запрашиваемой вещи.
     * @return True - вещь есть в хранилище, False - вещи нет в хранилище.
     */
    Boolean isExcludeItemById(Long id);

    /**
     * Удалить вещь с ID из хранилища.
     * @param id ID удаляемой вещи.
     */
    void removeItemById(Long id);

    /**
     * Удалить вещи пользователя с ID = userId.
     * @param userId ID пользователя, вещи которого надо удалить.
     */
    void removeItemsByUserId(Long userId);

    /**
     * Обновить вещь в БД.
     * @param item вещь.
     * @return обновлённая вещь.
     */
    Item updateInStorage(Item item, boolean[] isUpdateField);

    /**
     * Поиск вещей по тексту.
     * @param text текст.
     * @return список вещей.
     */
    List<Item> searchItemsByText(String text);
}
