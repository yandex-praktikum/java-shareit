package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestJpaRepository  extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findByUserId(int userId);

    @Query(" select r from ItemRequest r " +
            "where r.user.id <> ?1")
    List<ItemRequest> findAllRequests(int userId, Pageable page);
}
