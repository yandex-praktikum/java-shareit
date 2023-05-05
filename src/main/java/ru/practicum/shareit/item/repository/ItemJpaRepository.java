package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<Item, Integer> {

    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')))" +
            " and (i.available = true)")
    List<Item> search(String text, Pageable page);

    @Query(" select i from Item i " +
            "where i.owner.id = ?1")
    List<Item> findAll(int userId, Pageable page);

    @Query(" select i from Item i " +
            "where i.itemRequest.id = ?1")
    List<Item> findByRequestId(Integer id);
}
