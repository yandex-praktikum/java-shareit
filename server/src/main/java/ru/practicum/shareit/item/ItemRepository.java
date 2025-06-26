package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(User user);

    List<Item> findByOwnerId(Long id);

    List<Item> findByRequest_Id(Long id);

    @Modifying
    @Query("SELECT i FROM Item i JOIN FETCH i.owner WHERE i.available = true " +
            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')))")
    List<Item> searchItems(String text);
}
