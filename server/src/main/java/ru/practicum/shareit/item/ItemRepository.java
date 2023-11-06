package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(
            "select i from Item i " +
                    "where upper(i.name) like upper(concat('%', ?1, '%'))" +
            " or upper(i.description) like upper(concat('%', ?1, '%'))"
    )
    List<Item> findItemsByQuery(String query);

    List<Item> findItemsByOwnerId(Long ownerId);

    List<Item> findAllByRequestIdOrderByRequestCreatedDesc(Long requestId);
}
