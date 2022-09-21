package ru.practicum.shareit.item.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.request.ItemRequestMapper;
import ru.practicum.shareit.item.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.item.request.model.ItemRequest;
import ru.practicum.shareit.item.request.repository.ItemRequestRepository;
import ru.practicum.shareit.item.request.dto.ItemRequestDto;
import ru.practicum.shareit.item.request.dto.RequestDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    @Override
    public RequestDto create(long userId, RequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(checkUser(userId));
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getByRequestor(long userId) {
        User user = checkUser(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorOrderByCreatedDesc(user);
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : requests) {
            itemRequestDtoList.add(createItemRequestDto(itemRequest));
        }
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        if (from < 0) {
            throw new ItemRequestNotGoodParametrsException("from < 0");
        }
        User user = checkUser(userId);
        return itemRequestRepository.findAllByRequestorIsNot(user, PageRequest
                        .of(from / size, size, Sort.by(Sort.Direction.DESC, "created")))
                .stream()
                .map(this::createItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("ItemRequest not found"));
        return createItemRequestDto(itemRequest);
    }

    private ItemRequestDto createItemRequestDto(ItemRequest itemRequest) {
        List<ItemDto> items = itemRepository
                .findByRequest(itemRequest)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    public User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
    }
}
