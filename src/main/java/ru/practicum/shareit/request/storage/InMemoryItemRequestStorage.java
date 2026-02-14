package ru.practicum.shareit.request.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.*;

@Repository
public class InMemoryItemRequestStorage {
    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private long idCounter = 1;

    public ItemRequest create(ItemRequest request) {
        long id = idCounter++;
        request.setId(id);
        requests.put(id, request);
        return request;
    }

    public ItemRequest update(ItemRequest request) {
        if (!requests.containsKey(request.getId())) {
            throw new NotFoundException("Запрос вещи не найден");
        }

        requests.put(request.getId(), request);
        return request;
    }

    public Optional<ItemRequest> findById(Long id) {
        return Optional.ofNullable(requests.get(id));
    }
}
