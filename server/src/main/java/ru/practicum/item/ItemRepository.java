package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemById(Long itemId);

    Item findItemByOwnerId(Long ownerId);

    List<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE UPPER(i.name) like UPPER(CONCAT('%', ?1, '%')) " +
            " or UPPER(i.description) like UPPER(CONCAT('%', ?1, '%')) " +
            "and i.available = true")
    List<Item> searchItem(String text);

    List<Item> findItemsByRequestId(Long requestId);
}
