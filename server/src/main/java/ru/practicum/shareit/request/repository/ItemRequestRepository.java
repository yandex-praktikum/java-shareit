package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT new ru.practicum.shareit.request.dto.ItemRequestDto(ir.id, ir.description, ir.created) " +
            "FROM ItemRequest ir " +
            "WHERE ir.requestor.id = :requestorId " +
            "ORDER BY ir.created DESC")
    Collection<ItemRequestDto> findAllRequestsOfUser(@Param("requestorId") Long userId);

    @Query("SELECT new ru.practicum.shareit.request.dto.ItemRequestDto(ir.id, ir.description, ir.created) " +
            "FROM ItemRequest ir " +
            "WHERE ir.requestor.id <> :requestorId " +
            "ORDER BY ir.created DESC")
    Collection<ItemRequestDto> findAll(@Param("requestorId") Long userId);
}
