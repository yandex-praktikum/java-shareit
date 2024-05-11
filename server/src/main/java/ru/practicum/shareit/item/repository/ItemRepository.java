package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwnerId(int ownerId, PageRequest pageRequest);

    List<Item> findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(String nText, String dText, PageRequest pageRequest);

    List<Item> findAllByRequestId(int requestId);

}
