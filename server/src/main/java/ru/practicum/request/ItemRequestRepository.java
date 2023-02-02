package ru.practicum.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Long> {

    ItemRequest findRequestById(Long requestId);

    @Query("select r from ItemRequest r where r.user.id = ?1 order by r.created desc")
    List<ItemRequest> findRequestByRequestor_Id(Long requestorId);

    @Query("SELECT r FROM ItemRequest r where r.user.id <> ?1 order by r.created desc")
    List<ItemRequest> findAllRequests(Long userId, Pageable pageable);
}