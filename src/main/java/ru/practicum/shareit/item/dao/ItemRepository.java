package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findByOwner(User owner, Pageable page);

    List<Item> findByOwner(User owner);

    List<Item> findByRequestId(Integer requestId);

    @Query(nativeQuery = true, value = "SELECT * FROM ITEMS WHERE AVIABLE = true AND (upper(name) like %:text%  OR upper(description) like %:text%)")
    List<Item> findItemsLike(@Param("text") String text);


}
