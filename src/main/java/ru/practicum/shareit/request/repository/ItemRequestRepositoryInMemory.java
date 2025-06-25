package ru.practicum.shareit.request.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Repository
public class ItemRequestRepositoryInMemory implements ItemRequestRepository {

    private final Map<Long, ItemRequest> itemRequestMap = new HashMap<>();

    @Override
    public ItemRequest save(ItemRequest itemRequest) {
        if (itemRequest.getId() != null) {
            log.warn("нельзя создать запрос с id " + itemRequest.getId());
            throw new ValidationException("нельзя создать запрос с id " + itemRequest.getId());
        }
        itemRequest.setId(getNextId());
        itemRequestMap.put(itemRequest.getId(), itemRequest);
        log.info("создан запрос c id " + itemRequest.getId());
        return itemRequest;
    }


    @Override
    public ItemRequest update(ItemRequest itemRequest) {
        ItemRequest itemRequestOld = itemRequestMap.get(itemRequest.getId());
        if (itemRequest.getDescription() != null) {
            itemRequestOld.setDescription(itemRequest.getDescription());
        }
        if (itemRequest.getRequestor() != null) {
            itemRequestOld.setRequestor(itemRequest.getRequestor());
        }
        log.info("обновлен запрос c id " + itemRequest.getId());
        return itemRequestOld;
    }

    @Override
    public ItemRequest findById(long id) {
        log.info("запись запроса возвращена id " + id);
        return itemRequestMap.get(id);
    }

    @Override
    public Optional<ItemRequest> findByDescription(String description) {
        log.info("запрос по имени " + description);

        return itemRequestMap.values().stream()
                .filter(itemRequest -> itemRequest.getDescription().toLowerCase().equals(description))
                .findFirst();

    }

    @Override
    public Collection<ItemRequest> findAll() {
        log.info("весь список запросов");
        return itemRequestMap.values();
    }

    @Override
    public void delete(long id) {
        log.info("удаляем запрос id " + id);
        itemRequestMap.remove(id);
    }

    private long getNextId() {
        long currentMaxId = itemRequestMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
