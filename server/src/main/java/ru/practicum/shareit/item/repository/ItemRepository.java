package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT new ru.practicum.shareit.item.dto.item.ItemDto(" +
            "i.id, i.name, i.description, i.status = 'AVAILABLE', i.request.id) " +
            "FROM Item i WHERE i.owner.id = :ownerId")
    List<ItemDto> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT new ru.practicum.shareit.item.dto.item.ItemDto(" +
            "i.id, i.name, i.description, i.status = 'AVAILABLE', i.request.id) " +
            "FROM Item i " +
            "WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND i.status = 'AVAILABLE'")
    List<ItemDto> findFilteredItemsByOwnerId(@Param("text") String text);

    @Query("SELECT new ru.practicum.shareit.item.dto.item.ItemInItemRequestDto(" +
            "i.id, i.name, i.owner.id) " +
            "FROM Item i WHERE i.request.id = :requestId")
    List<ItemInItemRequestDto> findAllItemByRequestId(@Param("requestId") Long itemRequestId);
}
