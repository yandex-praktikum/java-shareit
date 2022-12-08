package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    @Query("select i from ItemRequest i where i.requestor.id = ?1 order by i.created desc")
    Collection<ItemRequest> getRequestsByRequestor(Integer requestorId);

    @Query("select ir from ItemRequest ir where ir.requestor.id <> ?1")
    Page<ItemRequest> getAll(Integer requestorId, Pageable pageable);

}
