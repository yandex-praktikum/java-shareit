package ru.practicum.shareit.item.dao;


import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;
public interface ItemDao {
    Item create(Item item);

    Optional<Item> findByUserIdAndItemId(Long userId, Long itemId);

    Optional<Item> findById(Long itemId);

    List<Item> findAllByUserId(Long userId);

    List<Item> findByText(String text);

    Item update(Item item, Long itemId);
}