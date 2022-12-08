package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private void checkUser(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ItemRequest add(ItemRequest itemRequest) {
        checkUser(itemRequest.getRequestor().getId());
        User user = userRepository.findById(itemRequest.getRequestor().getId()).orElseThrow();
        itemRequest.setRequestor(user);
        log.info("добавлен request=/{}/", itemRequest);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public Collection<ItemRequest> getAllOwn(Integer requestorId) {
        checkUser(requestorId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.getRequestsByRequestor(requestorId);
        if (itemRequests.isEmpty()) {
            return new ArrayList<ItemRequest>();
        }
        for (ItemRequest itemRequest : itemRequests) {
            itemRequest.setItems(itemRepository.findByRequestId(itemRequest.getId()));
        }
        log.info("запрошены requests пользователя /{}/", requestorId);
        return itemRequests;
    }

    @Override
    public ItemRequest getById(Integer requestId, Integer requestorId) {
        checkUser(requestorId);
        log.info("запрошен request /{}/ пользователя /{}/", requestId, requestorId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        itemRequest.setItems(itemRepository.findByRequestId(itemRequest.getId()));
        return itemRequest;

    }

    @Override
    public Collection<ItemRequest> getAll(Integer requestorId, Integer page, Integer size) {
        checkUser(requestorId);
        log.info(">>>===запрошены все request пользователем requestorId=/{}/ page=/{}/ size= /{}/===<<<<",
                requestorId, page, size);
        if (page < 0 || size < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Collection<ItemRequest> itemRequests =
                itemRequestRepository.getAll(requestorId, PageRequest.of(page, size)).toList();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequest.setItems(itemRepository.findByRequestId(itemRequest.getId()));
        }
        return itemRequests;
    }
}
