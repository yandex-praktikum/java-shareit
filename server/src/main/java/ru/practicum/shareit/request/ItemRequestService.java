package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.validators.PaginationValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    public ItemRequest getById(long requestId, long userId) {
        userService.getById(userId);
        Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(requestId);
        if (optionalItemRequest.isEmpty()) {
            throw new ItemRequestNotFoundException("Запрос на вещь " + requestId + " не найден");
        } else {
            return optionalItemRequest.get();
        }
    }

    public List<ItemRequest> getOwnItemRequests(long requestAuthorId) {
        userService.getById(requestAuthorId);
        return itemRequestRepository.findByRequestAuthor_idOrderByCreatedDesc(requestAuthorId);
    }

    public List<ItemRequest> getAll(int from, int size, long requestAuthorId) {
        Pageable page = PaginationValidator.validate(from, size);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findByRequestAuthor_idNotOrderByCreatedDesc(requestAuthorId, page);
        return itemRequestPage.getContent();
    }

    public ItemRequest create(ItemRequest itemRequest, long ownerId) {
        itemRequest.setRequestAuthor(userService.getById(ownerId));
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(itemRequest);
    }
}