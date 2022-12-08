package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("select o from Item o where (upper(o.name) like upper(concat('%',?1,'%')) "
            + "or upper(o.description) like upper(concat('%',?1,'%'))) and o.available=true")
    Page<Item> findByNameOrDesc(String text, Pageable pageable);

    Page<Item> findAllByOwnerOrderById(Integer owner, Pageable pageable);

    Set<Item> findByRequestId(Integer requestId);

    @Query(value = "select last_value from items_id_seq", nativeQuery = true)
    Integer findLastValue();

}
