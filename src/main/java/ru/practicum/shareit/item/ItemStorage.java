package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    /**
     * Creates a new item
     *
     * @param item
     * @return new item
     */
    Item createItem(Item item);

    /**
     * Updates the item
     *
     * @param item
     * @return updated item
     */
    Item updateItem(Item item);

    /**
     * Returns item by id
     * If the item is not found returns null
     *
     * @param itemId
     * @return item or null if there was no one
     */
    Item getItemById(Long itemId);

    /**
     * Returns all items by ownerId
     *
     * @param ownerId
     * @return List of items
     */
    List<Item> getUserItems(long ownerId);

    /**
     * Find items with text
     * If text equals "" return empty ArrayList
     *
     * @param text
     * @return List of items
     */
    List<Item> findItemsWithText(String text);
}