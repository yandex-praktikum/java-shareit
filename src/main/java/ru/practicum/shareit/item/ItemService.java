package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    /**
     * Creates a new item
     *
     * @param item
     * @return new item
     */
    Item createItem(Item item);

    /**
     * Updates the item
     * If the current user is not the owner of the item throws NotFoundException
     *
     * @param item
     * @return updated item
     */
    Item updateItem(Item item);

    /**
     * Returns item by id
     * If the item is not found throws NotFoundException
     *
     * @param itemId
     * @return item by id
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
     *
     * @param text
     * @return List of items
     */
    List<Item> findItemsWithText(String text);
}
