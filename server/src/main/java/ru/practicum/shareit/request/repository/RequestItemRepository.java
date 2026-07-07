package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.RequestItem;

import java.util.List;

public interface RequestItemRepository extends JpaRepository<RequestItem, Long> {

    List<RequestItem> findAllByRequestorIdOrderByCreatedDesc(Long ownerId);
}