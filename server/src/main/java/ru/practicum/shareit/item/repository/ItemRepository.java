package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findByOwnerId(Long ownerId);

    @Query("""
    SELECT i
    FROM Item i
    WHERE i.available = true
        AND (
            LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%'))
                OR (i.description) LIKE LOWER(CONCAT('%', :text, '%'))
            )
    """)
    List<Item> search(String text);
}
