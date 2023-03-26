package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestAuthor_idOrderByCreatedDesc(long requestAuthorId);

    Page<ItemRequest> findByRequestAuthor_idNotOrderByCreatedDesc(long requestAuthorId, Pageable page);
}
