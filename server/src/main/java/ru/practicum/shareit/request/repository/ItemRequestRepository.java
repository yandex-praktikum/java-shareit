package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("""
        select distinct r
        from ItemRequest r
        left join fetch r.items
        where r.requester.id = :userId
        order by r.created desc
        """)
    List<ItemRequest> findAllByRequesterId(@Param("userId") Long userId);

    @Query("""
        select distinct r
        from ItemRequest r
        left join fetch r.items
        where r.requester.id <> :userId
        order by r.created desc
        """)
    List<ItemRequest> findAllExceptRequester(@Param("userId") Long userId);
}