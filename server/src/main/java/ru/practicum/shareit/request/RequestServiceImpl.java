package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestWithTimeDto create(ItemRequestDto itemRequestDto, Long userId) {
        log.info("Добавление запроса");
        validateUserId(userId);
        ItemRequest itemRequest = RequestMapper.fromItemRequestDto(itemRequestDto, userId, LocalDateTime.now());
        ItemRequest savedItemRequest = requestRepository.save(itemRequest);
        ItemRequestWithTimeDto itemRequestWithTimeDto = RequestMapper.toItemRequestWithTimeDto(savedItemRequest);
        return itemRequestWithTimeDto;
    }

    @Override
    public List<ItemRequestFullDto> findAllMyRequests(Long ownerId) {
        log.info("Поиск моих запросов");
        validateUserId(ownerId);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(ownerId);
        List<ItemRequestFullDto> itemRequestsFullDto = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(itemRequest.getId());
            if (items.isEmpty()) {
                items = new ArrayList<>();
            }
            List<ItemDto> itemsDto = ItemMapper.toItemsDto(items);
            ItemRequestFullDto itemRequestFullDto = RequestMapper.toItemRequestFullDto(itemRequest, itemsDto);
            itemRequestsFullDto.add(itemRequestFullDto);
        }
        return itemRequestsFullDto;
    }

    @Transactional
    @Override
    public List<ItemRequestFullDto> findAllRequestsByOtherUsers(Long ownerId, Long from, Long size) {
        log.info("Поиск чужих запросов");
        validateUserId(ownerId);
        if ((from == null) || (size == null)) {
            return new ArrayList<>();
        } else if (((from == 0) && (size == 0)) || (size <= 0)) {
            throw new ValidationException("");
        }
        Page<ItemRequest> requestsByPage = requestRepository
                .findAll(PageRequest.of(from.intValue(), size.intValue()));
        List<ItemRequest> requests = requestsByPage.toList();
        List<ItemRequestFullDto> sortedRequest = new ArrayList<>();
        for (ItemRequest request : requests) {
            if (!request.getRequesterId().equals(ownerId)) {
                List<Item> items = itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(request.getId());
                List<ItemDto> itemsDto = ItemMapper.toItemsDto(items);
                sortedRequest.add(RequestMapper.toItemRequestFullDto(request, itemsDto));
            }
        }
        return sortedRequest;
    }

    @Override
    public ItemRequestFullDto findById(Long userId, Long requestId) {
        validateUserId(userId);
        Optional<ItemRequest> itemRequest = requestRepository.findById(requestId);
        if (itemRequest.isEmpty()) {
            throw new NotFoundException("Запрос с id " + requestId + "не найден");
        }
        List<Item> items = itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(itemRequest.get().getId());
        List<ItemDto> itemsDto = ItemMapper.toItemsDto(items);
        return RequestMapper.toItemRequestFullDto(itemRequest.get(), itemsDto);
    }

    @Transactional
    private void validateUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User с id " + userId + " не найден")
        );
    }

}
