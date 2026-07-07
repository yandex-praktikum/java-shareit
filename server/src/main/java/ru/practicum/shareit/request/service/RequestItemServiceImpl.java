package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestItemServiceImpl implements RequestItemService {
    private final RequestItemRepository repository;
    private final UserService userService;

    @Override
    @Transactional
    public RequestItem createRequestItem(RequestItem requestItem, Long requestorId) {
        User requestor = userService.getUserById(requestorId);
        requestItem.setRequestor(requestor);
        requestItem.setCreated(Instant.now());
        return repository.save(requestItem);
    }

    @Override
    public List<RequestItem> getAllRequestItemByOwner(Long ownerId) {
        return repository.findAllByRequestorIdOrderByCreatedDesc(ownerId);
    }

    @Override
    public List<RequestItem> getAllRequestItemAllUsers(Long currentUserId) {
        return repository.findAll().stream()
                .filter(r -> !r.getRequestor().getId().equals(currentUserId))
                .toList();
    }

    @Override
    public RequestItem getRequestItemById(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }
}