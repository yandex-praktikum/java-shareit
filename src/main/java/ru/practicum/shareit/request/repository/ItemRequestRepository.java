package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.Optional;

public interface ItemRequestRepository {


    ItemRequest save(ItemRequest itemRequest);

    ItemRequest update(ItemRequest itemRequest);

    ItemRequest findById(long id);

    Optional<ItemRequest> findByDescription(String description);

    Collection<ItemRequest> findAll();

    void delete(long id);


}
