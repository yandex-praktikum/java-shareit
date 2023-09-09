package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long id, Pageable pageable);

    Optional<Item> findByIdAndAndAvailable(long id, boolean available);

    @Modifying
    @Query(value = "UPDATE ITEMS it SET NAME = coalesce(:name, NAME),\n" +
            "    DESCRIPTION = coalesce(:description, DESCRIPTION),\n" +
            "    IS_AVAILABLE = coalesce(:available, IS_AVAILABLE),\n" +
            "    REQUEST_ID = coalesce(:requestId, REQUEST_ID)\n" +
            "    WHERE ID =:id AND OWNER_ID = :ownerId", nativeQuery = true)
    Integer update(@Param("id") Long id,
                   @Param("ownerId") Long ownerId,
                   @Param("name") String name,
                   @Param("description") String description,
                   @Param("available") Boolean available,
                   @Param("requestId") Long requestId
    );

    @Query("SELECT i FROM Item i WHERE" +
            "(lower(i.name) like concat('%',lower(:value),'%')" +
            "OR lower(i.description) like concat('%',lower(:value),'%'))" +
            "AND i.available IS true ")
    List<Item> searchByValue(@Param("value") String value, Pageable pageable);

    Integer deleteByIdAndOwner_Id(long ownerId, long itemId);

}
