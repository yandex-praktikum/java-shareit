package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long requestorId) {
        checkUserExists(requestorId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestorId(requestorId);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsByRequestor(Long requestorId) {
        checkUserExists(requestorId);
        return itemRequestRepository.findByRequestorId(requestorId).stream()
                .filter(request -> request.getRequestorId().equals(requestorId))
                .sorted((r1, r2) -> r2.getCreated().compareTo(r1.getCreated()))
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, int from, int size) {
        checkUserExists(userId);

        List<ItemRequest> allRequests = itemRequestRepository.findByRequestorIdNot(userId).stream()
                .filter(request -> !request.getRequestorId().equals(userId))
                .sorted((r1, r2) -> r2.getCreated().compareTo(r1.getCreated()))
                .collect(Collectors.toList());


        int start = Math.min(from, allRequests.size());
        int end = Math.min(from + size, allRequests.size());

        return allRequests.subList(start, end).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Запрос с ID " + requestId + " не найден"));

        List<Item> items = itemRepository.findByRequestId(requestId);

        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }
}

