package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Collection<Item> findAllByOwnerId(int userId);

    @Query("SELECT i FROM Item as i " +
            "WHERE i.available IS TRUE " +
            "  AND (UPPER(i.name) LIKE UPPER(CONCAT('%',?1,'%')) " +
            "       OR UPPER(i.description) LIKE UPPER(CONCAT('%',?1,'%')))")
    Collection<Item> searchItems(String text);

    Collection<Item> findAllByRequestId(int requestId);
}
